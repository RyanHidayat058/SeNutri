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
import com.example.covary.databinding.ActivityQuestionnaireFourthBinding
import com.example.covary.databinding.ActivityQuestionnaireThirdBinding

class QuestionnaireFourthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireFourthBinding
    private var selectedValue: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireFourthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            insets
        }

        val rbEmpatSatu: RadioButton = findViewById(R.id.rbEmpatSatu)
        val rbEmpatDua: RadioButton = findViewById(R.id.rbEmpatDua)
        val rbEmpatTiga: RadioButton = findViewById(R.id.rbEmpatTiga)

        rbEmpatSatu.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbEmpatDua.buttonTintList = ColorStateList.valueOf(Color.WHITE)
        rbEmpatTiga.buttonTintList = ColorStateList.valueOf(Color.WHITE)

        val radioButtons = listOf(rbEmpatSatu, rbEmpatDua, rbEmpatTiga)

        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                for (rb in radioButtons) {
                    rb.isChecked = rb == radioButton
                }
                selectedValue = when (radioButton) {
                    rbEmpatSatu -> 3.0
                    rbEmpatDua -> 3.6
                    rbEmpatTiga -> 5.0
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

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnBerikutnya.setOnClickListener {
            if (radioButtons.any { it.isChecked }) {
                val intent = Intent(this, QuestionnaireFifthActivity::class.java)
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
                intent.putExtra("NILAI_JAWABAN_KEEMPAT", selectedValue)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Harap pilih setidaknya satu opsi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}