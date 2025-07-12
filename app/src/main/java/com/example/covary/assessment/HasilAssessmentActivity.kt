package com.example.covary.assessment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.MainActivity
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
        val usiaUser = intent.getIntExtra("USIA_USER", 0)
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")
        val berat = intent.getStringExtra("BERAT_USER")
        val tinggi = intent.getStringExtra("TINGGI_USER")
        val penyakitString = intent.getStringExtra("PENYAKIT_USER")
        Log.d("DEBUG", "Penyakit diterima: $penyakitString")
        val target = intent.getDoubleExtra("TARGET_USER", 0.0)
        val kuesioner1 = intent.getDoubleExtra("NILAI_JAWABAN_PERTAMA", 0.0)
        val kuesioner2 = intent.getDoubleExtra("NILAI_JAWABAN_KEDUA", 0.0)
        val kuesioner3 = intent.getDoubleExtra("NILAI_JAWABAN_KETIGA", 0.0)
        val kuesioner4 = intent.getDoubleExtra("NILAI_JAWABAN_KEEMPAT", 0.0)
        val kuesioner5 = intent.getDoubleExtra("NILAI_JAWABAN_KELIMA", 0.0)

        val beratString = berat?.toDoubleOrNull() ?: 0.0
        val tinggiString = tinggi?.toDoubleOrNull() ?: 0.0
        val usiaDouble = usiaUser.toDouble()

        val tinggiMeter = tinggiString / 100.0
        val imt = beratString / (tinggiMeter * tinggiMeter)
        val hitungPAL = (kuesioner1 + kuesioner2 + kuesioner3 + kuesioner4 + kuesioner5) / 10.0

        //Hitung BMR
        val bmr = when (gender) {
            "Laki-laki" -> (10.0 * beratString) + (6.25 * tinggiString) - (5.0 * usiaDouble) + 5.0
            "Perempuan" -> (10.0 * beratString) + (6.25 * tinggiString) - (5.0 * usiaDouble) - 161.0
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
        val totalKalori = tdee.toDouble()
        val maxKarbohidrat = ((0.65 * totalKalori) / 4)
        val maxProtein = ((0.20 * totalKalori) / 4)
        val maxLemak = ((0.30 * totalKalori) / 9)

        val karbohidrat = (maxKarbohidrat * 10).roundToInt() / 10.0
        val protein = (maxProtein * 10).roundToInt() / 10.0
        val lemak = (maxLemak * 10).roundToInt() / 10.0

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

        binding.btnBerikutnya.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val updatedUserData = hashMapOf(
                "nama" to nama,
                "usia" to usiaUser,
                "tanggal_lahir" to tanggalLahir,
                "jenisKelamin" to gender,
                "berat" to berat,
                "tinggi" to tinggi,
                "penyakit" to penyakitString,
                "target" to tujuan,
                "kategoriIMT" to kategoriIMT,
                "asupan_kalori" to tdee,
                "maksimal_karbohidrat" to karbohidrat,
                "maksimal_protein" to protein,
                "maksimal_lemak" to lemak
            )

            db.collection("users").document(userId)
                .set(updatedUserData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onBackPressed() {
        // Kosongkan method ini agar tombol back tidak berfungsi
        super.onBackPressed()
    }
}