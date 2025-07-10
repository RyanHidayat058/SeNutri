package com.example.covary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R
import com.example.covary.adapterHistory.DetailMakananAdapter
import com.example.covary.adapterHistory.MakananDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailMakananAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var tanggal: String
    private lateinit var tanggalFormatted: String

    private val jenisMakananList = listOf("Sarapan", "Makan Siang", "Camilan", "Makan Malam")
    private val listDetail = mutableListOf<MakananDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvDetailMakanan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        tanggal = arguments?.getString("tanggal") ?: return

        val rawTanggal = arguments?.getString("tanggal") ?: return
        tanggal = rawTanggal

        val formatterInput = java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", java.util.Locale("id"))
        val formatterOutput = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale("id"))

        tanggalFormatted = try {
            val date = formatterInput.parse(rawTanggal)
            formatterOutput.format(date)
        } catch (e: Exception) {
            Log.e("DetailHistory", "Format tanggal salah: $rawTanggal")
            rawTanggal
        }

        view.findViewById<TextView>(R.id.tvTanggalDetail).text = tanggal

        // Tombol back
        val backButton: ImageButton = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        fetchAllMakanan(view)
    }

    private fun fetchAllMakanan(view: View) {
        var selesai = 0

        for (jenis in jenisMakananList) {
            val ref = firestore.collection("users").document(userId)
                .collection("history").document(tanggal)
                .collection(jenis)

            ref.get().addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    val data = doc.data ?: continue
                    val item = MakananDetail(
                        nama = data["nama"].toString(),
                        jenisMakanan = jenis,
                        kalori = data["kalori"].toString().toDoubleOrNull() ?: 0.0,
                        karbohidrat = data["karbohidrat"].toString().toDoubleOrNull() ?: 0.0,
                        protein = data["protein"].toString().toDoubleOrNull() ?: 0.0,
                        lemak = data["lemak"].toString().toDoubleOrNull() ?: 0.0,
                        gram = (data["gram"] as? Number)?.toInt() ?: 0,
                        jam = data["jam"]?.toString() ?: ""
                    )
                    listDetail.add(item)
                }
                selesai++
                if (selesai == jenisMakananList.size) {
                    adapter = DetailMakananAdapter(listDetail)
                    recyclerView.adapter = adapter
                    updateAsupanUI(view)
                }
            }
        }
    }

    private fun updateAsupanUI(view: View) {
        val totalKalori = listDetail.sumOf { it.kalori }
        val totalKarbo = listDetail.sumOf { it.karbohidrat }
        val totalProtein = listDetail.sumOf { it.protein }
        val totalLemak = listDetail.sumOf { it.lemak }

        val userDocRef = firestore.collection("users").document(userId)

        userDocRef.get().addOnSuccessListener { userSnapshot ->
            val maxKalori = userSnapshot.getDouble("asupan_kalori") ?: 0.0
            val maxKarbo = userSnapshot.getDouble("maksimal_karbohidrat") ?: 0.0
            val maxProtein = userSnapshot.getDouble("maksimal_protein") ?: 0.0
            val maxLemak = userSnapshot.getDouble("maksimal_lemak") ?: 0.0

            view.findViewById<TextView>(R.id.tvKaloriDH).text =
                "${String.format("%.1f", totalKalori)} / ${String.format("%.1f", maxKalori)}g"
            view.findViewById<TextView>(R.id.tvKarboDH).text =
                "${String.format("%.1f", totalKarbo)} / ${String.format("%.1f", maxKarbo)}g"
            view.findViewById<TextView>(R.id.tvProteinDH).text =
                "${String.format("%.1f", totalProtein)} / ${String.format("%.1f", maxProtein)}g"
            view.findViewById<TextView>(R.id.tvLemakDH).text =
                "${String.format("%.1f", totalLemak)} / ${String.format("%.1f", maxLemak)}g"

            view.findViewById<ProgressBar>(R.id.progressKaloriDH).progress =
                ((totalKalori / maxKalori) * 100).coerceIn(0.0, 100.0).toInt()
            view.findViewById<ProgressBar>(R.id.progressKarboDH).progress =
                ((totalKarbo / maxKarbo) * 100).coerceIn(0.0, 100.0).toInt()
            view.findViewById<ProgressBar>(R.id.progressProteinDH).progress =
                ((totalProtein / maxProtein) * 100).coerceIn(0.0, 100.0).toInt()
            view.findViewById<ProgressBar>(R.id.progressLemakDH).progress =
                ((totalLemak / maxLemak) * 100).coerceIn(0.0, 100.0).toInt()

            Log.d("DetailHistory", "Mengakses dokumen cairan: users/$userId/asupanCairan/$tanggalFormatted")

            userDocRef.collection("asupanCairan").document(tanggalFormatted).get()
                .addOnSuccessListener { cairanSnapshot ->
                    val volume = when (val v = cairanSnapshot.get("volumeMl")) {
                        is Number -> v.toInt()
                        is String -> v.toIntOrNull() ?: 0
                        else -> 0
                    }

                    Log.d("DetailHistory", "Volume hasil parsing: $volume")
                    Log.d("DetailHistory", "volumeMl: ${cairanSnapshot.get("volumeMl")}")
                    Log.d("DetailHistory", "volumeMl Tipe: ${cairanSnapshot.get("volumeMl")?.javaClass?.name}")

                    val maxCairan = 3000
                    view.findViewById<TextView>(R.id.tvAsupanCairanDH).text = "$volume / $maxCairan ml"
                    view.findViewById<ProgressBar>(R.id.progressAsupanCairanDH).progress =
                        ((volume.toDouble() / maxCairan) * 100).coerceIn(0.0, 100.0).toInt()
                }
        }
    }
}