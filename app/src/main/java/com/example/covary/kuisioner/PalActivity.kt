package com.example.covary.kuisioner

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.covary.R
import com.example.covary.databinding.ActivityPalBinding
import com.example.covary.databinding.ActivityQuestionnaireFirstBinding

class PalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityPalBinding.inflate(layoutInflater)

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
        Log.d("DEBUG", "Penyakit diterima: $penyakitString")

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            val intent = Intent(this, QuestionnaireFirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("NAMA_USER", nama)
            intent.putExtra("USIA_USER", usia)
            intent.putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
            intent.putExtra("GENDER_USER", gender)
            intent.putExtra("BERAT_USER", berat)
            intent.putExtra("TINGGI_USER", tinggi)
            intent.putExtra("PENYAKIT_USER", penyakitString)
            intent.putExtra("TARGET_USER", target)
            startActivity(intent)
        }
    }
}