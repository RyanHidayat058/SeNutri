package com.example.covary.login

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
import com.example.covary.databinding.ActivityLoginBinding
import com.example.covary.forgotpassword.ForgotPasswordActivity
import com.example.covary.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        binding.tvLupaKataSandi.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.password.text.toString().trim()

            if (email.isEmpty()) {
                binding.email.error = "Email tidak boleh kosong"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.password.error = "Kata sandi tidak boleh kosong"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, AssessmentFirstActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


        val passwordEditText = findViewById<EditText>(R.id.password)
        var isPasswordVisible = false

        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordEditText.compoundDrawablesRelative[2]

                if (drawableEnd != null && event.rawX >= (passwordEditText.right - drawableEnd.bounds.width() - 50)) {
                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordEditText.transformationMethod = null
                        passwordEditText.post {
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_lock, 0, R.drawable.ic_hidden_eye, 0
                            )
                        }
                    } else {
                        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordEditText.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
                        passwordEditText.post {
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_lock, 0, R.drawable.ic_eye, 0
                            )
                        }
                    }

                    passwordEditText.setSelection(passwordEditText.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
    override fun onBackPressed() {

    }

    //Kalo mau nyoba dari awal, ini dihapus
    /**override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null ){
            val intent = Intent(this, AssessmentFirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }**/
}