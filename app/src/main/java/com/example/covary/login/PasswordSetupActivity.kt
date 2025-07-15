package com.example.covary.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.covary.MainActivity
import com.example.covary.R
import com.example.covary.assessment.AssessmentFirstActivity
import com.example.covary.login.utils.SessionManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ImageButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.covary.login.LoginActivity

class PasswordSetupActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var btnSimpan: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_setup)

        etPassword = findViewById(R.id.etPassword)
        btnSimpan = findViewById(R.id.btnSimpanPassword)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Toggle password visibility
        etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etPassword.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (etPassword.right - drawableEnd.bounds.width() - 50)) {
                    isPasswordVisible = !isPasswordVisible

                    if (isPasswordVisible) {
                        etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_hidden_eye, 0)
                    } else {
                        etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
                    }

                    etPassword.setSelection(etPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)

// Inisialisasi GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnBack.setOnClickListener {
            firebaseAuth.signOut()
            googleSignInClient.signOut()

            val sessionManager = SessionManager(this)
            sessionManager.setHasLoggedIn(false)

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnSimpan.setOnClickListener {
            val passwordBaru = etPassword.text.toString().trim()
            val user = firebaseAuth.currentUser
            val email = user?.email

            if (passwordBaru.isEmpty()) {
                etPassword.error = "Kata Sandi tidak boleh kosong"
                return@setOnClickListener
            }

            if (passwordBaru.length < 8) {
                etPassword.error = "Kata Sandi minimal 8 karakter"
                return@setOnClickListener
            }

            if (user != null && email != null) {
                val credential = EmailAuthProvider.getCredential(email, passwordBaru)

                user.linkWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // ✅ Update Firestore: isPasswordCreated = true
                            firestore.collection("users").document(user.uid)
                                .update("isPasswordCreated", true)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Kata Sandi berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Gagal memperbarui status password", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Gagal menyimpan Kata Sandi.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}