package com.example.covary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R
import com.example.covary.adapterMakanan.Makanan
import com.example.covary.adapterMakanan.MakananAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CamilanFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var makananAdapter: MakananAdapter
    private val listMakanan = mutableListOf<Makanan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camilan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: ImageButton = view.findViewById(R.id.btnBack)
        val titleTextView: TextView = view.findViewById(R.id.tvTitle)
        titleTextView.text = "Camilan"
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.rvCamilan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchDataMakanMalam()
    }

    private fun fetchDataMakanMalam() {
        val uid = auth.currentUser?.uid ?: return
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        val currentDate = dateFormat.format(Calendar.getInstance().time)
        val path = "users/$uid/history/$currentDate/Camilan"

        db.collection(path).get()
            .addOnSuccessListener { result ->
                var totalKalori = 0.0
                var totalKarbo = 0.0
                var totalProtein = 0.0
                var totalLemak = 0.0

                listMakanan.clear()
                for (doc in result) {
                    val kalori = doc.getDouble("kalori") ?: 0.0
                    val karbo = doc.getDouble("karbohidrat") ?: 0.0
                    val protein = doc.getDouble("protein") ?: 0.0
                    val lemak = doc.getDouble("lemak") ?: 0.0

                    totalKalori += kalori
                    totalKarbo += karbo
                    totalProtein += protein
                    totalLemak += lemak

                    val namaMakanan = doc.getString("nama") ?: ""
                    val gram = doc.getDouble("gram")?.toInt() ?: 0
                    val jam = doc.getString("jam") ?: ""

                    val makanan = Makanan(
                        nama = "$namaMakanan ($gram g) • $jam",
                        kalori = kalori,
                        karbohidrat = karbo,
                        lemak = lemak,
                        protein = protein
                    )
                    listMakanan.add(makanan)
                }

                makananAdapter = MakananAdapter(listMakanan)
                recyclerView.adapter = makananAdapter

                // Panggil fungsi untuk ambil data maksimal dari user
                fetchUserData(totalKalori, totalKarbo, totalProtein, totalLemak)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat data.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchUserData(
        totalKalori: Double,
        totalKarbo: Double,
        totalProtein: Double,
        totalLemak: Double
    ) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = db.collection("users").document(uid)

        userRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val maksimalKalori = document.getDouble("asupan_kalori") ?: 0.0
                val maksimalKarbo = document.getDouble("maksimal_karbohidrat") ?: 0.0
                val maksimalProtein = document.getDouble("maksimal_protein") ?: 0.0
                val maksimalLemak = document.getDouble("maksimal_lemak") ?: 0.0

                val persenKalori = if (maksimalKalori != 0.0) (totalKalori / maksimalKalori * 100).toInt() else 0
                val persenKarbo = if (maksimalKarbo != 0.0) (totalKarbo / maksimalKarbo * 100).toInt() else 0
                val persenProtein = if (maksimalProtein != 0.0) (totalProtein / maksimalProtein * 100).toInt() else 0
                val persenLemak = if (maksimalLemak != 0.0) (totalLemak / maksimalLemak * 100).toInt() else 0

                view?.apply {
                    findViewById<TextView>(R.id.tvKaloriCamilan).text =
                        "${String.format("%.1f", totalKalori)}/${String.format("%.1f", maksimalKalori)}g"
                    findViewById<TextView>(R.id.tvKarboCamilan).text =
                        "${String.format("%.1f", totalKarbo)}/${String.format("%.1f", maksimalKarbo)}g"
                    findViewById<TextView>(R.id.tvProteinCamilan).text =
                        "${String.format("%.1f", totalProtein)}/${String.format("%.1f", maksimalProtein)}g"
                    findViewById<TextView>(R.id.tvLemakCamilan).text =
                        "${String.format("%.1f", totalLemak)}/${String.format("%.1f", maksimalLemak)}g"

                    findViewById<ProgressBar>(R.id.progressKaloriCamilan).progress = persenKalori
                    findViewById<ProgressBar>(R.id.progressKarboCamilan).progress = persenKarbo
                    findViewById<ProgressBar>(R.id.progressProteinCamilan).progress = persenProtein
                    findViewById<ProgressBar>(R.id.progressLemakCamilan).progress = persenLemak
                }
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Gagal memuat data user.", Toast.LENGTH_SHORT).show()
        }
    }
}