package com.example.covary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covary.R
import com.example.covary.retrofit.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailDaftarMakananFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_daftar_makanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jenisMakanan = arguments?.getString("jenisMakanan")
        val menuItem = arguments?.getParcelable<MenuItem>("menuItem")

        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvKalori: TextView = view.findViewById(R.id.tvCalories)
        val tvKarbohidrat: TextView = view.findViewById(R.id.tvCarbo)
        val tvProtein: TextView = view.findViewById(R.id.tvProtein)
        val tvLemak: TextView = view.findViewById(R.id.tvFat)
        val tvPorsi: TextView = view.findViewById(R.id.tvPorsi)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)

        val gramInt = menuItem?.gram?.replace("g", "")?.toIntOrNull() ?: 0
        val baseGram = gramInt
        val baseKalori = menuItem?.kalori ?: 0.0
        val baseKarbo = menuItem?.karbohidrat ?: 0.0
        val baseProtein = menuItem?.protein ?: 0.0
        val baseLemak = menuItem?.lemak ?: 0.0

        var porsi = 1

        fun updateNutritionDisplay() {
            val totalGram = baseGram * porsi
            val totalKalori = baseKalori * porsi
            val totalKarbo = baseKarbo * porsi
            val totalProtein = baseProtein * porsi
            val totalLemak = baseLemak * porsi
            val jamSekarang = SimpleDateFormat("HH:mm", Locale("id", "ID")).format(Calendar.getInstance().time)

            tvNama.text = "${menuItem?.nama} (${totalGram}g) • $jamSekarang"
            tvKalori.text = "Kalori: ${"%.1f".format(totalKalori)}g"
            tvKarbohidrat.text = "Karbohidrat: ${"%.1f".format(totalKarbo)}g"
            tvProtein.text = "Protein: ${"%.1f".format(totalProtein)}g"
            tvLemak.text = "Lemak: ${"%.1f".format(totalLemak)}g"
            tvPorsi.text = porsi.toString()
        }

        updateNutritionDisplay()

        btnPlus.setOnClickListener {
            porsi++
            updateNutritionDisplay()
        }

        btnMinus.setOnClickListener {
            if (porsi > 1) {
                porsi--
                updateNutritionDisplay()
            }
        }

        // Tombol back
        val backButton: ImageButton = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val btnKonfirmasi: Button = view.findViewById(R.id.btnKonfirmasiUbahKataSandi)
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        btnKonfirmasi.setOnClickListener {
            val currentDate = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
            val formattedDate = dateFormat.format(currentDate)

            val data = hashMapOf(
                "jenisMakanan" to jenisMakanan,
                "tanggal" to formattedDate,
                "jam" to SimpleDateFormat("HH:mm", Locale("id", "ID")).format(currentDate),
                "nama" to menuItem?.nama,
                "gram" to (baseGram * porsi),
                "kalori" to "%.1f".format(Locale.ENGLISH, baseKalori * porsi).toDouble(),
                "karbohidrat" to "%.1f".format(Locale.ENGLISH, baseKarbo * porsi).toDouble(),
                "protein" to "%.1f".format(Locale.ENGLISH, baseProtein * porsi).toDouble(),
                "lemak" to "%.1f".format(Locale.ENGLISH, baseLemak * porsi).toDouble(),
                "porsi" to porsi
            )

            db.collection("users")
                .document(userId)
                .collection("history")
                .document(formattedDate)
                .collection(jenisMakanan ?: "lainnya")
                .add(data)
                .addOnSuccessListener {
                    // ✅ Tambahkan ke historyIndex
                    val indexRef = db.collection("users")
                        .document(userId)
                        .collection("historyIndex")
                        .document("index")

                    indexRef.update("tanggal", com.google.firebase.firestore.FieldValue.arrayUnion(formattedDate))
                        .addOnSuccessListener {
                            Log.d("IndexUpdate", "Tanggal $formattedDate ditambahkan ke index")
                        }
                        .addOnFailureListener { e ->
                            // Kalau index belum ada, buat
                            indexRef.set(mapOf("tanggal" to listOf(formattedDate)))
                                .addOnSuccessListener {
                                    Log.d("IndexUpdate", "Index dibuat dan tanggal ditambahkan")
                                }
                                .addOnFailureListener { err ->
                                    Log.e("IndexUpdate", "Gagal membuat index", err)
                                }
                        }

                    // ⏩ Lanjut ke DailyFragment
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, DailyFragment())
                        .commit()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Gagal ngirim data", e)
                }
        }
    }
}