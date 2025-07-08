package com.example.covary.assessment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.databinding.ActivityAssessmentFirstBinding
import com.example.covary.forgotpassword.ForgotPasswordActivity
import com.example.covary.login.LoginActivity
import com.example.covary.register.RegisterActivity

class AssessmentFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentFirstBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            insets
        }

        binding.btnBerikutnya.setOnClickListener {
            val nama = binding.namaKamu.text.toString().trim()

            if (nama.isEmpty()) {
                binding.namaKamu.error = "Nama harus diisi"
                binding.namaKamu.requestFocus()
            } else {
                val intent = Intent(this, AssessmentSecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("NAMA_USER", nama)
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        // Kosongkan method ini agar tombol back tidak berfungsi
    }
}