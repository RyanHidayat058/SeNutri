package com.example.covary.assessment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityAssessmentFifthBinding
import com.example.covary.kuisioner.PalActivity
import com.example.covary.kuisioner.QuestionnaireFirstActivity

class AssessmentFifthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentFifthBinding
    private var selectedValue: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityAssessmentFifthBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }

        val rbMenaikan = binding.rbMenaikan
        val rbMenjaga = binding.rbMenjaga
        val rbMenurunkan = binding.rbMenurunkan

        rbMenaikan.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbMenjaga.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbMenurunkan.buttonTintList = ColorStateList.valueOf(Color.WHITE)

        val radioButtons = listOf(rbMenaikan, rbMenjaga, rbMenurunkan)

        for (radioButton in radioButtons) {
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    for (rb in radioButtons) {
                        rb.isChecked = rb == buttonView
                    }
                    selectedValue = when (buttonView) {
                        rbMenaikan -> 3.0
                        rbMenjaga -> 3.6
                        rbMenurunkan -> 5.0
                        else -> null
                    }
                }
            }
        }

        val nama = intent.getStringExtra("NAMA_USER")
        val usia = intent.getStringExtra("USIA_USER")
        val tanggalLahir = intent.getStringExtra("TANGGAL_LAHIR_USER")
        val gender = intent.getStringExtra("GENDER_USER")
        val berat = intent.getStringExtra("BERAT_USER")
        val tinggi = intent.getStringExtra("TINGGI_USER")
        val penyakitString = intent.getStringExtra("PENYAKIT_USER")
        Log.d("DEBUG", "Penyakit diterima: $penyakitString")

        binding.haloUser.text = "Halo, $nama. Mari kita mulai dari target Anda."

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            if (radioButtons.any { it.isChecked }) {
                val intent = Intent(this, PalActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("NAMA_USER", nama)
                intent.putExtra("USIA_USER", usia)
                intent.putExtra("TANGGAL_LAHIR_USER", tanggalLahir)
                intent.putExtra("GENDER_USER", gender)
                intent.putExtra("BERAT_USER", berat)
                intent.putExtra("TINGGI_USER", tinggi)
                intent.putExtra("PENYAKIT_USER", penyakitString)
                intent.putExtra("TARGET_USER", selectedValue)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}