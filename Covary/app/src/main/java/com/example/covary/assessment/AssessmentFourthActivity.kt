package com.example.covary.assessment

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityAssessmentFourthBinding

class AssessmentFourthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityAssessmentFourthBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }
        val cbLainnya = findViewById<CheckBox>(R.id.cbLainnya)
        val etLainnya = findViewById<EditText>(R.id.etLainnya)

        cbLainnya.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etLainnya.visibility = android.view.View.VISIBLE
            } else {
                etLainnya.visibility = android.view.View.GONE
            }
        }

        //Menerima data dari activity sebelumnya
        val nama = intent.getStringExtra("NAMA_USER")
        val usia = intent.getStringExtra("USIA_USER")
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")
        val berat = intent.getStringExtra("BERAT_USER")
        val tinggi = intent.getStringExtra("TINGGI_USER")

        binding.btnBerikutnya.setOnClickListener {
            val checkboxes = listOf(
                binding.cbTumor,
                binding.cbDiabetes,
                binding.cbHipertensi,
                binding.cbKolesterol,
                binding.cbAsamUrat,
                binding.cbTidakTahu,
                binding.cbLainnya
            )

            if (checkboxes.none { it.isChecked }) {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi!", Toast.LENGTH_SHORT).show()
            } else {
                // Ambil semua checkbox yang dicentang, lalu ambil teksnya
                val penyakitList = checkboxes.filter { it.isChecked }.map { it.text.toString() }

                // Kalau "Lainnya" dicentang, ambil juga text dari EditText
                if (binding.cbLainnya.isChecked) {
                    val lainnyaText = binding.etLainnya.text.toString().trim()
                    if (lainnyaText.isNotEmpty()) {
                        penyakitList.plus(lainnyaText)
                    }
                }

                // Gabungkan list penyakit menjadi satu string, misal dipisahkan koma
                val penyakitString = penyakitList.joinToString(", ")

                val intent = Intent(this, AssessmentFifthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("NAMA_USER", nama)
                intent.putExtra("USIA_USER", usia)
                intent.putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
                intent.putExtra("GENDER_USER", gender)
                intent.putExtra("BERAT_USER", berat)
                intent.putExtra("TINGGI_USER", tinggi)
                intent.putExtra("PENYAKIT_USER", penyakitString)
                startActivity(intent)
            }
        }
    }
}