package com.example.covary.assessment

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityHasilAssessmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlin.math.roundToInt

class HasilAssessmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilAssessmentBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityHasilAssessmentBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }

        val nama = intent.getStringExtra("NAMA_USER")
        val usia = intent.getStringExtra("USIA_USER")
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")
        val berat = intent.getStringExtra("BERAT_USER")
        val tinggi = intent.getStringExtra("TINGGI_USER")
        val penyakitString = intent.getStringExtra("PENYAKIT_USER")
        val target = intent.getDoubleExtra("TARGET_USER", 0.0)
        val kuesioner1 = intent.getDoubleExtra("NILAI_JAWABAN_PERTAMA", 0.0)
        val kuesioner2 = intent.getDoubleExtra("NILAI_JAWABAN_KEDUA", 0.0)
        val kuesioner3 = intent.getDoubleExtra("NILAI_JAWABAN_KETIGA", 0.0)
        val kuesioner4 = intent.getDoubleExtra("NILAI_JAWABAN_KEEMPAT", 0.0)
        val kuesioner5 = intent.getDoubleExtra("NILAI_JAWABAN_KELIMA", 0.0)

        val beratString = berat?.toDoubleOrNull() ?: 0.0
        val tinggiString = tinggi?.toDoubleOrNull() ?: 0.0
        val usiaString = usia?.toDoubleOrNull() ?: 0.0

        val tinggiMeter = tinggiString / 100.0
        val imt = beratString / (tinggiMeter * tinggiMeter)
        val hitungPAL = (kuesioner1 + kuesioner2 + kuesioner3 + kuesioner4 + kuesioner5) / 10.0

        //Hitung BMR
        val bmr = when (gender) {
            "Laki-laki" -> (10.0 * beratString) + (6.25 * tinggiString) - (5.0 * usiaString) + 5.0
            "Perempuan" -> (10.0 * beratString) + (6.25 * tinggiString) - (5.0 * usiaString) - 161.0
            else -> 0.0
        }

        // Tentukan PAL
        val pal = when {
            hitungPAL < 1.59 -> "Sedikit AKtif"
            hitungPAL in 1.6..1.9 -> "Aktif"
            hitungPAL in 1.91..2.51 -> "Sangat Aktif"
            else -> "Data tidak valid"
        }

        //Hitung TDEE
        val tdee = (bmr * hitungPAL).roundToInt()

        // Tentukan kategori IMT
        val kategoriIMT = when {
            imt < 18.5 -> "Underweight"
            imt in 18.5..22.9 -> "Normal"
            imt in 23.0..24.9 -> "Overweight"
            imt >= 25.0 -> "Obesitas"
            else -> "Data tidak valid"
        }

        val tujuan = when (target) {
            3.0 -> "Menaikan berat badan"
            3.6 -> "Menjaga berat badan"
            5.0 -> "Menurunkan berat badan"
            else -> "Tidak diketahui"
        }

        binding.tvTujuan2HA.text = "$tujuan"
        binding.tvBeratBadan2HA.text = "$berat kg"
        binding.tvTinggiBadan2HA.text = "$tinggi cm"
        binding.tvIndeksMasaTubuh2HA.text = "$kategoriIMT"
        binding.tvKategoriPAL2HA.text = "$pal"
        binding.tvAsupanKaloriHarian2HA.text = "$tdee"

        /**val userMap = hashMapOf(
            "name" to nama,
            "usia" to usia,
            "tanggal_lahir" to tanggalLahir,
            "gender" to gender,
            "berat" to berat,
            "tinggi" to tinggi,
            "penyakit" to penyakitString,
            "target" to target,
        )

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("user").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                
            }**/
    }
    override fun onBackPressed() {
        // Kosongkan method ini agar tombol back tidak berfungsi
    }
}