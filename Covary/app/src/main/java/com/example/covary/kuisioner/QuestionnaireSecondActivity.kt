package com.example.covary.kuisioner

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityQuestionnaireSecondBinding

class QuestionnaireSecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireSecondBinding
    private var selectedValue: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding sebelum enableEdgeToEdge()
        binding = ActivityQuestionnaireSecondBinding.inflate(layoutInflater)

        enableEdgeToEdge()  // Pastikan dipanggil setelah binding diinisialisasi
        setContentView(binding.root)

        // Pastikan binding.root tidak null sebelum dipakai
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            // Implementasi listener di sini
            insets
        }

        val rbDuaSatu: RadioButton = findViewById(R.id.rbDuaSatu)
        val rbDuaDua: RadioButton = findViewById(R.id.rbDuaDua)
        val rbDuaTiga: RadioButton = findViewById(R.id.rbDuaTiga)

        rbDuaSatu.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbDuaDua.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbDuaTiga.buttonTintList = ColorStateList.valueOf(Color.WHITE)

        val radioButtons = listOf(rbDuaSatu, rbDuaDua, rbDuaTiga)

        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                for (rb in radioButtons) {
                    rb.isChecked = rb == radioButton // Memastikan hanya satu yang terpilih
                }
                // Set value berdasarkan pilihan user
                selectedValue = when (radioButton) {
                    rbDuaSatu -> 3.0
                    rbDuaDua -> 3.6
                    rbDuaTiga -> 5.0
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
        val target = intent.getDoubleExtra("TARGET_USER", 0.0)
        val kuesioner1 = intent.getDoubleExtra("NILAI_JAWABAN_PERTAMA", 0.0)

        binding.btnBerikutnya.setOnClickListener {
            if (radioButtons.any { it.isChecked }) {
                val intent = Intent(this, QuestionnaireThirdActivity::class.java)
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
                intent.putExtra("NILAI_JAWABAN_KEDUA", selectedValue)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}