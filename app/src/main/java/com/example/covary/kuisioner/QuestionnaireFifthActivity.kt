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
import com.example.covary.assessment.HasilAssessmentActivity
import com.example.covary.databinding.ActivityQuestionnaireFifthBinding
import com.example.covary.databinding.ActivityQuestionnaireFourthBinding

class QuestionnaireFifthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireFifthBinding
    private var selectedValue: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityQuestionnaireFifthBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }

        val rbLimaSatu: RadioButton = findViewById(R.id.rbLimaSatu)
        val rbLimaDua: RadioButton = findViewById(R.id.rbLimaDua)
        val rbLimaTiga: RadioButton = findViewById(R.id.rbLimaTiga)

        rbLimaSatu.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbLimaDua.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbLimaTiga.buttonTintList = ColorStateList.valueOf(Color.WHITE)

        val radioButtons = listOf(rbLimaSatu, rbLimaDua, rbLimaTiga)

        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                for (rb in radioButtons) {
                    rb.isChecked = rb == radioButton // Memastikan hanya satu yang terpilih
                }
                selectedValue = when (radioButton) {
                    rbLimaSatu -> 3.0
                    rbLimaDua -> 3.6
                    rbLimaTiga -> 5.0
                    else -> null
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
        val target = intent.getDoubleExtra("TARGET_USER", 0.0)
        val kuesioner1 = intent.getDoubleExtra("NILAI_JAWABAN_PERTAMA", 0.0)
        val kuesioner2 = intent.getDoubleExtra("NILAI_JAWABAN_KEDUA", 0.0)
        val kuesioner3 = intent.getDoubleExtra("NILAI_JAWABAN_KETIGA", 0.0)
        val kuesioner4 = intent.getDoubleExtra("NILAI_JAWABAN_KEEMPAT", 0.0)

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            if (radioButtons.any { it.isChecked }) {
                val intent = Intent(this, YakinActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("NAMA_USER", nama)
                intent.putExtra("USIA_USER", usia)
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
                intent.putExtra("NILAI_JAWABAN_KELIMA", selectedValue)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}