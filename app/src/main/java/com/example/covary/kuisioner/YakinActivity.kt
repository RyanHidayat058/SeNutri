package com.example.covary.kuisioner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.covary.MainActivity
import com.example.covary.R
import com.example.covary.assessment.HasilAssessmentActivity
import com.example.covary.databinding.ActivityHasilAssessmentBinding
import com.example.covary.databinding.ActivityYakinBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class YakinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYakinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityYakinBinding.inflate(layoutInflater)

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
        Log.d("DEBUG", "Penyakit diterima: $penyakitString")
        val target = intent.getDoubleExtra("TARGET_USER", 0.0)
        val kuesioner1 = intent.getDoubleExtra("NILAI_JAWABAN_PERTAMA", 0.0)
        val kuesioner2 = intent.getDoubleExtra("NILAI_JAWABAN_KEDUA", 0.0)
        val kuesioner3 = intent.getDoubleExtra("NILAI_JAWABAN_KETIGA", 0.0)
        val kuesioner4 = intent.getDoubleExtra("NILAI_JAWABAN_KEEMPAT", 0.0)
        val kuesioner5 = intent.getDoubleExtra("NILAI_JAWABAN_KELIMA", 0.0)

        val tujuan = when (target) {
            3.0 -> "Menaikan berat badan"
            3.6 -> "Menjaga berat badan"
            5.0 -> "Menurunkan berat badan"
            else -> "Tidak diketahui"
        }

        val kuesionerSatu = when (kuesioner1) {
            3.0 -> "Jarang Sekali"
            3.6 -> "Kadang-Kadang"
            5.0 -> "Sering"
            else -> "Tidak diketahui"
        }

        val kuesionerDua = when (kuesioner2) {
            3.0 -> "Duduk hampir sepanjang hari"
            3.6 -> "Berdiri atau bergerak sebagian waktu"
            5.0 -> "Banyak aktivitas fisik"
            else -> "Tidak diketahui"
        }

        val kuesionerTiga = when (kuesioner3) {
            3.0 -> "Tidak pernah atau jarang sekali"
            3.6 -> "1-3 kali per minggu"
            5.0 -> "4 kali atau lebih per minggu"
            else -> "Tidak diketahui"
        }

        val kuesionerEmpat = when (kuesioner4) {
            3.0 -> "Kurang dari 30 menit"
            3.6 -> "30 menit - 1 jam"
            5.0 -> "Lebih dari 1 jam"
            else -> "Tidak diketahui"
        }

        val kuesionerLima = when (kuesioner5) {
            3.0 -> "Ringan"
            3.6 -> "Sedang"
            5.0 -> "Berat"
            else -> "Tidak diketahui"
        }

        val usiaUser = hitungUsia(tanggalLahir.toString())

        binding.namaUser.text = "Nama: $nama"
        binding.usiaUser.text = "Usia: $usiaUser tahun"
        binding.tanggalLahirUser.text = "$tinggi cm"
        binding.genderUser.text = "Jenis Kelamin: $gender"
        binding.beratUser.text = "Berat: $berat kg"
        binding.tinggiUser.text = "Tinggi: $tinggi cm"
        binding.penyakitUser.text = "Penyakit: $penyakitString"
        binding.targetUser.text = "Target: $tujuan"
        binding.kuesioner1.text = "Pada aktivitas sehari-hari, seberapa sering Anda melakukan aktivitas fisik dalam sehari-hari? (misalnya, berjalan kaki, naik tangga, pekerjaan rumah tangga).\nJawabanmu: $kuesionerSatu"
        binding.kuesioner2.text = "Pada aktivitas kerja, apa jenis pekerjaan atau aktivitas utama Anda sehari-hari?\nJawabanmu: $kuesionerDua"
        binding.kuesioner3.text = "Pada aktivitas olahraga, seberapa sering Anda melakukan olahraga atau latihan fisik dalam seminggu?\nJawabanmu: $kuesionerTiga"
        binding.kuesioner4.text = "Pada durasi aktivitas fisik harian, berapa total Anda beraktivitas fisik dalam sehari? (termasuk pekerjaan, olahraga, dan kegiatan harian).\nJawabanmu: $kuesionerEmpat"
        binding.kuesioner5.text = "Pada intensitas aktivitas fisik, bagaimana intensitas fisik yang Anda lakukan sehari-hari?\nJawabanmu: $kuesionerLima"

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnYa.setOnClickListener {
            val intent = Intent(this, HasilAssessmentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("NAMA_USER", nama)
            intent.putExtra("USIA_USER", usiaUser)
            intent.putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
            intent.putExtra("GENDER_USER", gender)
            intent.putExtra("BERAT_USER", berat)
            intent.putExtra("TINGGI_USER", tinggi)
            intent.putExtra("PENYAKIT_USER", penyakitString)
            intent.putExtra("TARGET_USER", target)
            intent.putExtra("NILAI_JAWABAN_PERTAMA", kuesioner1)
            intent.putExtra("NILAI_JAWABAN_KEDUA", kuesioner2)
            intent.putExtra("NILAI_JAWABAN_KETIGA", kuesioner3)
            intent.putExtra("NILAI_JAWABAN_KEEMPAT", kuesioner4)
            intent.putExtra("NILAI_JAWABAN_KELIMA", kuesioner5)
            startActivity(intent)
        }
    }

    fun hitungUsia(tanggalLahirStr: String): Int {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val tanggalLahir = sdf.parse(tanggalLahirStr) ?: return 0

        val lahir = Calendar.getInstance()
        lahir.time = tanggalLahir

        val hariIni = Calendar.getInstance()

        var usiauser = hariIni.get(Calendar.YEAR) - lahir.get(Calendar.YEAR)

        // Jika bulan dan hari ini belum lewat dibandingkan dengan tanggal lahir, kurangi 1 tahun
        if (hariIni.get(Calendar.DAY_OF_YEAR) < lahir.get(Calendar.DAY_OF_YEAR)) {
            usiauser--
        }

        return usiauser
    }
    override fun onBackPressed() {
        // Kosongkan method ini agar tombol back tidak berfungsi
        super.onBackPressed()
    }
}