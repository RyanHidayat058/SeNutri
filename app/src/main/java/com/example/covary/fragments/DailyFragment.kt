package com.example.covary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.covary.MainActivity
import com.example.covary.R
import com.example.covary.databinding.FragmentDailyBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class DailyFragment : Fragment() {

    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!

    private var targetKarbo: Double = 0.0
    private var targetProtein: Double = 0.0
    private var targetLemak: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardSarapan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SarapanFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.cardMakanSiang.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MakanSiangFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.cardCamilan.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CamilanFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.cardMakanMalam.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MakanMalamFragment())
                .addToBackStack(null)
                .commit()
        }

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val tanggal = SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            Locale("id", "ID")
        ).format(Calendar.getInstance().time)

        val jenisMakananList = listOf("Sarapan", "Makan Siang", "Camilan", "Makan Malam")

        var totalKalori = 0.0
        var totalKarbohidrat = 0.0
        var totalProtein = 0.0
        var totalLemak = 0.0

        val kaloriPerJenis = mutableMapOf<String, Double>()
        val totalIdMap = mutableMapOf<String, Int>()
        var jenisSelesai = 0

// Ambil nilai kebutuhan dari akun user (target maksimal)
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val asupanKalori = document.getLong("asupan_kalori")?.toInt() ?: 0
                    binding.tvKaloriCount.text = "0/$asupanKalori"
                    binding.progressKalori.max = asupanKalori
                    binding.progressKalori.progress = 0
                    
                    targetKarbo = document.getDouble("maksimal_karbohidrat") ?: 0.0
                    targetProtein = document.getDouble("maksimal_protein") ?: 0.0
                    targetLemak = document.getDouble("maksimal_lemak") ?: 0.0

                    binding.tvKarbohidrat.text = "0/${String.format("%.1f", targetKarbo)}g"
                    binding.tvProtein.text = "0/${String.format("%.1f", targetProtein)}g"
                    binding.tvLemak.text = "0/${String.format("%.1f", targetLemak)}g"

                    val namaUser = document.getString("nama") ?: ""
                    binding.tvWelcome.text = "Selamat Datang $namaUser"
                }
            }

        jenisMakananList.forEach { jenis ->
            db.collection("users").document(userId)
                .collection("history").document(tanggal)
                .collection(jenis)
                .get()
                .addOnSuccessListener { documents ->
                    var kalori = 0.0
                    var karbo = 0.0
                    var protein = 0.0
                    var lemak = 0.0

                    for (doc in documents) {
                        kalori += doc.getDouble("kalori") ?: 0.0
                        karbo += doc.getDouble("karbohidrat") ?: 0.0
                        protein += doc.getDouble("protein") ?: 0.0
                        lemak += doc.getDouble("lemak") ?: 0.0
                    }

                    totalKalori += kalori
                    totalKarbohidrat += karbo
                    totalProtein += protein
                    totalLemak += lemak

                    kaloriPerJenis[jenis] = kalori
                    totalIdMap[jenis] = documents.size()

                    jenisSelesai++
                    if (jenisSelesai == jenisMakananList.size) {
                        updateGiziUI(totalKalori, totalKarbohidrat, totalProtein, totalLemak, totalIdMap, kaloriPerJenis, tanggal)
                    }
                }
                .addOnFailureListener {
                    kaloriPerJenis[jenis] = 0.0
                    totalIdMap[jenis] = 0
                    jenisSelesai++
                    if (jenisSelesai == jenisMakananList.size) {
                        updateGiziUI(totalKalori, totalKarbohidrat, totalProtein, totalLemak, totalIdMap, kaloriPerJenis, tanggal)
                    }
                }
        }
    }

    private fun navigateToDaftarMakananDenganJenis(jenisMakanan: String) {
        val fragment = DaftarMakananFragment()
        val bundle = Bundle()
        bundle.putString("jenisMakanan", jenisMakanan)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun updateGiziUI(
        kalori: Double,
        karbo: Double,
        protein: Double,
        lemak: Double,
        totalIdMap: Map<String, Int>,
        kaloriPerJenis: Map<String, Double>,
        tanggal: String
    ) {
        val kaloriFormatted = String.format("%.1f", kalori)
        val maxKalori = binding.progressKalori.max

        binding.tvKaloriCount.text = "$kaloriFormatted/$maxKalori"
        binding.progressKalori.progress = kalori.roundToInt()

        val persenKalori = if (maxKalori != 0) (kalori / maxKalori) * 100 else 0.0
        binding.percentageKalori.text = "${persenKalori.roundToInt()}%"

        binding.tvKarbohidrat.text = "${String.format("%.1f", karbo)}/${String.format("%.1f", targetKarbo)}g"
        binding.tvProtein.text = "${String.format("%.1f", protein)}/${String.format("%.1f", targetProtein)}g"
        binding.tvLemak.text = "${String.format("%.1f", lemak)}/${String.format("%.1f", targetLemak)}g"

        binding.tanggalHariIni.text = tanggal

        binding.totalSarapan.text =
            "Total Makanan: ${totalIdMap["Sarapan"] ?: 0}\nTotal Kalori: ${String.format("%.1f", kaloriPerJenis["Sarapan"] ?: 0.0)} Kalori"
        binding.totalMakanSiang.text =
            "Total Makanan: ${totalIdMap["Makan Siang"] ?: 0}\nTotal Kalori: ${String.format("%.1f", kaloriPerJenis["Makan Siang"] ?: 0.0)} Kalori"
        binding.totalCamilan.text =
            "Total Makanan: ${totalIdMap["Camilan"] ?: 0}\nTotal Kalori: ${String.format("%.1f", kaloriPerJenis["Camilan"] ?: 0.0)} Kalori"
        binding.totalMakanMalam.text =
            "Total Makanan: ${totalIdMap["Makan Malam"] ?: 0}\nTotal Kalori: ${String.format("%.1f", kaloriPerJenis["Makan Malam"] ?: 0.0)} Kalori"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}