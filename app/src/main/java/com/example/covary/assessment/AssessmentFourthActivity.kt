package com.example.covary.assessment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityAssessmentFourthBinding

class AssessmentFourthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentFourthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { _, insets -> insets }

        binding.cbLainnya.setOnCheckedChangeListener { _, isChecked ->
            binding.etLainnya.visibility = if (isChecked) android.view.View.VISIBLE else android.view.View.GONE
            if (!isChecked) binding.etLainnya.text.clear()
        }

        val nama = intent.getStringExtra("NAMA_USER")
        val usia = intent.getStringExtra("USIA_USER")
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")
        val berat = intent.getStringExtra("BERAT_USER")
        val tinggi = intent.getStringExtra("TINGGI_USER")

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            val checkboxes = listOf(
                binding.cbTumor,
                binding.cbDiabetes,
                binding.cbHipertensi,
                binding.cbKolesterol,
                binding.cbAsamUrat,
                binding.cbTidakTahu
            )

            val isAnyChecked = checkboxes.any { it.isChecked } ||
                    (binding.cbLainnya.isChecked && binding.etLainnya.text.toString().isNotBlank())

            if (!isAnyChecked) {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi atau isi kolom 'Lainnya'", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil semua penyakit yang dicentang
            val penyakitList = mutableListOf<String>()
            checkboxes.forEach {
                if (it.isChecked) {
                    when (it.id) {
                        R.id.cbTumor -> penyakitList.add("Tumor/Kanker")
                        R.id.cbDiabetes -> penyakitList.add("Diabetes")
                        R.id.cbHipertensi -> penyakitList.add("Hipertensi")
                        R.id.cbKolesterol -> penyakitList.add("Kolesterol")
                        R.id.cbAsamUrat -> penyakitList.add("Asam Urat")
                        R.id.cbTidakTahu -> penyakitList.add("Tidak Tahu")
                    }
                }
            }

            // Jika 'Lainnya' dicentang dan diisi, tambahkan ke list
            if (binding.cbLainnya.isChecked) {
                val lainnyaText = binding.etLainnya.text.toString().trim()
                if (lainnyaText.isNotEmpty()) {
                    penyakitList.add(lainnyaText)
                }
            }

            // Gabungkan dengan koma dan pastikan tidak kosong
            val penyakitString = penyakitList.filter { it.isNotBlank() }.joinToString(", ")

            // Kirim ke activity berikutnya
            val intent = Intent(this, AssessmentFifthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("NAMA_USER", nama)
                putExtra("USIA_USER", usia)
                putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
                putExtra("GENDER_USER", gender)
                putExtra("BERAT_USER", berat)
                putExtra("TINGGI_USER", tinggi)
                putExtra("PENYAKIT_USER", penyakitString)
            }

            Log.d("DEBUG", "Dikirim ke next activity: $penyakitString")
            checkboxes.forEach {
                Log.d("CHECKBOX", "${it.text}: ${it.isChecked}")
            }
            startActivity(intent)
        }
    }
}