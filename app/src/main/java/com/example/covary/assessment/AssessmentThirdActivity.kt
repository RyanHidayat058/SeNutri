package com.example.covary.assessment

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.covary.R
import com.example.covary.databinding.ActivityAssessmentSecondBinding
import com.example.covary.databinding.ActivityAssessmentThirdBinding

class AssessmentThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentThirdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityAssessmentThirdBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }

        val beratEditText = findViewById<EditText>(R.id.beratKamu)
        val tinggiEditText = findViewById<EditText>(R.id.tinggiKamu)

        //Menerima data dari activity sebelumnya
        val nama = intent.getStringExtra("NAMA_USER")
        val usia = intent.getStringExtra("USIA_USER")
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            val berat = beratEditText.text.toString().trim()
            val tinggi = tinggiEditText.text.toString().trim()

            if (berat.isEmpty()) {
                beratEditText.error = "Harap masukkan berat Anda"
                beratEditText.requestFocus()
                return@setOnClickListener
            }
            if (!berat.matches(Regex("\\d+"))) {
                Toast.makeText(this, "Harap isi berat dengan angka saja", Toast.LENGTH_SHORT).show()
                beratEditText.error = "Harap isi dengan angka"
                beratEditText.requestFocus()
                return@setOnClickListener
            }

            if (tinggi.isEmpty()) {
                beratEditText.error = "Harap masukkan tinggi Anda"
                beratEditText.requestFocus()
                return@setOnClickListener
            }
            if (!tinggi.matches(Regex("\\d+"))) {
                Toast.makeText(this, "Harap isi tinggi dengan angka saja", Toast.LENGTH_SHORT).show()
                tinggiEditText.error = "Harap isi dengan angka"
                tinggiEditText.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, AssessmentFourthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("NAMA_USER", nama)
            intent.putExtra("USIA_USER", usia)
            intent.putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
            intent.putExtra("GENDER_USER", gender)
            intent.putExtra("BERAT_USER", berat)
            intent.putExtra("TINGGI_USER", tinggi)
            startActivity(intent)
        }
    }
}