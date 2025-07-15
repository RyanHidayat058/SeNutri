package com.example.covary.register

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
import com.example.covary.R
import com.example.covary.assessment.AssessmentFirstActivity
import com.example.covary.databinding.ActivityRegisterBinding
import com.example.covary.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler
import android.os.Looper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.root.setOnApplyWindowInsetsListener { v, insets -> insets }
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Setup Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.tvLogin.setOnClickListener {
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
                !email.matches(Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$")) -> {
                    binding.email.error = "Gunakan alamat email yang valid (contoh: nama@gmail.com)"
                    binding.email.requestFocus()
                }
                pass.isEmpty() -> {
                    binding.password.error = "Kata sandi tidak boleh kosong"
                    binding.password.requestFocus()
                }
                pass.length < 8 -> {
                    binding.password.error = "Gunakan kata sandi dengan paling sedikit delapan karakter."
                    binding.password.requestFocus()
                }
                confirmPass.isEmpty() -> {
                    binding.cPassword.error = "Konfirmasi kata sandi tidak boleh kosong"
                    binding.cPassword.requestFocus()
                }
                pass != confirmPass -> {
                    Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                    binding.btnBuatAkun.isEnabled = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.btnBuatAkun.isEnabled = true

                            if (it.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {
                                            val userData = hashMapOf(
                                                "nama" to "",
                                                "usia" to 0,
                                                "jenisKelamin" to "",
                                                "email" to email
                                            )
                                            firestore.collection("users")
                                                .document(user.uid)
                                                .set(userData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this, "Verifikasi email telah dikirim. Periksa kotak masuk atau spam pada email Anda.", Toast.LENGTH_LONG).show()
                                                    startActivity(Intent(this, LoginActivity::class.java))
                                                    finish()
                                                }
                                        } else {
                                            Toast.makeText(this, "Gagal mengirim email verifikasi", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Email sudah digunakan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, 1000)
                }
            }
        }

        // Listener tombol daftarDenganGoogle untuk Google Sign-In
        binding.daftarDenganGoogle.setOnClickListener {
            signInWithGoogle()
        }

        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.cPassword)

        setupPasswordToggle(passwordEditText)
        setupPasswordToggle(confirmPasswordEditText)
    }

    private fun signInWithGoogle() {
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
                Toast.makeText(this, "Google sign in gagal atau periksa koneksi anda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
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
                                        val intent = Intent(this, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Daftar dengan google gagal atau periksa koneksi anda", Toast.LENGTH_SHORT).show()
                }
            }
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
        super.onBackPressed()
    }
}