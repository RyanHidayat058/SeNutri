package com.example.covary.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.R
import com.example.covary.assessment.AssessmentFirstActivity
import com.example.covary.databinding.ActivityRegisterBinding
import com.example.covary.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnBuatAkun.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.password.text.toString().trim()
            val confirmPass = binding.cPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.email.error = "Email tidak boleh kosong"
                    binding.email.requestFocus()
                }
                pass.isEmpty() -> {
                    binding.password.error = "Kata sandi tidak boleh kosong"
                    binding.password.requestFocus()
                }
                confirmPass.isEmpty() -> {
                    binding.cPassword.error = "Konfirmasi kata sandi tidak boleh kosong"
                    binding.cPassword.requestFocus()
                }
                pass != confirmPass -> {
                    binding.cPassword.error = "Kata sandi tidak cocok"
                    binding.cPassword.requestFocus()
                }
                else -> {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, AssessmentFirstActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.cPassword)

        setupPasswordToggle(passwordEditText)
        setupPasswordToggle(confirmPasswordEditText)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle(editText: EditText) {
        var isPasswordVisible = false

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                if (event.rawX >= (editText.right - editText.compoundDrawables[drawableEnd].bounds.width())) {
                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_eye, 0)
                    } else {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_hidden_eye, 0)
                    }

                    editText.setSelection(editText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    override fun onBackPressed() {

    }
}