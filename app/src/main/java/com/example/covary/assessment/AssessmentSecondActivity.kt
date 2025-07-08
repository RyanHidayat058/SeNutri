package com.example.covary.assessment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.databinding.ActivityAssessmentSecondBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AssessmentSecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssessmentSecondBinding
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentSecondBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val datePickerLayout = findViewById<RelativeLayout>(R.id.datePickerLayout)
        val tvTanggalLahir = findViewById<TextView>(R.id.tvTanggalLahir)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        datePickerLayout.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = dateFormat.format(calendar.time)
                    tvTanggalLahir.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        val genderSpinner: Spinner = findViewById(R.id.spinner_gender)
        val genderOptions = arrayOf("Pilih Jenis Kelamin", "Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        genderSpinner.adapter = adapter

        val usiaEditText = findViewById<EditText>(R.id.usiaKamu)
        val btnBerikutnya = binding.btnBerikutnya

        val nama = intent.getStringExtra("NAMA_USER")

        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnBerikutnya.setOnClickListener {
            val usia = usiaEditText.text.toString().trim()
            val gender = genderSpinner.selectedItem.toString()

            if (usia.isEmpty()) {
                Toast.makeText(this, "Harap masukkan berapa usia Anda", Toast.LENGTH_SHORT).show()
                usiaEditText.requestFocus()
                return@setOnClickListener
            }

            if (!usia.matches(Regex("\\d+"))) {
                Toast.makeText(this, "Harap isi usia dengan angka saja", Toast.LENGTH_SHORT).show()
                usiaEditText.error = "Harap isi dengan angka"
                usiaEditText.requestFocus()
                return@setOnClickListener
            }

            if (selectedDate == null) {
                Toast.makeText(this, "Harap pilih tanggal lahir", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (gender == "Pilih Jenis Kelamin") {
                Toast.makeText(this, "Harap pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, AssessmentThirdActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("NAMA_USER", nama)
            intent.putExtra("USIA_USER", usia)
            intent.putExtra("TANGGAL_LAHIR_USER", selectedDate)
            intent.putExtra("GENDER_USER", gender)
            startActivity(intent)
        }
    }
}