package com.example.covary.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.covary.MainActivity
import com.example.covary.R
import com.example.covary.assessment.AssessmentFirstActivity
import com.example.covary.databinding.ActivityLoginBinding
import com.example.covary.forgotpassword.ForgotPasswordActivity
import com.example.covary.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler
import android.os.Looper
import com.example.covary.login.utils.SessionManager


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 101

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val sessionManager = SessionManager(this)
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && currentUser.isEmailVerified && sessionManager.hasLoggedIn()) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val nama = document.getString("nama")
                    if (nama.isNullOrEmpty()) {
                        startActivity(Intent(this, AssessmentFirstActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    finish()
                }
            }.addOnFailureListener {
                // Gagal baca Firestore
            }
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { v, insets -> insets }

        // Setup Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Tombol Register
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Tombol Lupa Password
        binding.tvLupaKataSandi.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Tombol Login dengan Email & Password
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

            // Tampilkan progress bar & nonaktifkan tombol
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnLogin.isEnabled = false

            // Delay 4 detik sebelum eksekusi login
            Handler(Looper.getMainLooper()).postDelayed({
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.btnLogin.isEnabled = true

                    if (it.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null && user.isEmailVerified) {
                            sessionManager.setHasLoggedIn(true)
                            val userDocRef = firestore.collection("users").document(user.uid)
                            userDocRef.get().addOnSuccessListener { document ->
                                val nama = document.getString("nama")
                                if (nama.isNullOrEmpty()) {
                                    startActivity(Intent(this, AssessmentFirstActivity::class.java))
                                } else {
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Gagal membaca data", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            firebaseAuth.signOut()
                            Toast.makeText(this, "Email Anda belum diverifikasi. Silakan periksa kotak masuk Anda.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Masuk dengan email gagal atau periksa koneksi anda", Toast.LENGTH_SHORT).show()
                    }
                }
            }, 1000) // <-- Delay 4 detik
        }


        // Tombol Login dengan Google
        binding.masukDenganGoogle.setOnClickListener {
            signInWithGoogle()
        }

        // Password visibility toggle
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

    // Fungsi memulai Google Sign-In
    private fun signInWithGoogle() {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnLogin.isEnabled = false
        binding.masukDenganGoogle.isEnabled = false

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Masuk dengan Google gagal atau periksa koneksi anda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Autentikasi Firebase dengan Google Token
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val user = firebaseAuth.currentUser
                        user?.let { currentUser ->
                            val userDocRef = firestore.collection("users").document(currentUser.uid)
                            userDocRef.get().addOnSuccessListener { document ->
                                if (!document.exists()) {
                                    val userData = hashMapOf(
                                        "nama" to "",
                                        "usia" to 0,
                                        "jenisKelamin" to "",
                                        "email" to currentUser.email.orEmpty()
                                    )
                                    userDocRef.set(userData)
                                        .addOnSuccessListener {
                                            SessionManager(this).setHasLoggedIn(true)

                                            continueAfterLogin(userDocRef)
                                        }
                                        .addOnFailureListener {
                                            binding.progressBar.visibility = android.view.View.GONE
                                            binding.btnLogin.isEnabled = true
                                            binding.masukDenganGoogle.isEnabled = true
                                            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    SessionManager(this).setHasLoggedIn(true)

                                    continueAfterLogin(userDocRef)
                                }
                            }.addOnFailureListener {
                                binding.progressBar.visibility = android.view.View.GONE
                                binding.btnLogin.isEnabled = true
                                binding.masukDenganGoogle.isEnabled = true
                                Toast.makeText(this, "Gagal membaca data user", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, 1000) // ⏳ Delay 4 detik sebelum baca firestore
                } else {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.btnLogin.isEnabled = true
                    binding.masukDenganGoogle.isEnabled = true
                    Toast.makeText(this, "Autentikasi Firebase gagal", Toast.LENGTH_SHORT).show()
                }

            }
    }


    private fun continueAfterLogin(userDocRef: com.google.firebase.firestore.DocumentReference) {
        userDocRef.get().addOnSuccessListener { document ->
            binding.progressBar.visibility = android.view.View.GONE
            binding.btnLogin.isEnabled = true
            binding.masukDenganGoogle.isEnabled = true

            val nama = document.getString("nama")
            if (nama.isNullOrEmpty()) {
                startActivity(Intent(this, AssessmentFirstActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }.addOnFailureListener {
            binding.progressBar.visibility = android.view.View.GONE
            binding.btnLogin.isEnabled = true
            binding.masukDenganGoogle.isEnabled = true
            Toast.makeText(this, "Gagal membaca data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}