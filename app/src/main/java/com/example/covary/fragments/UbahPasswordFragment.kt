package com.example.covary.fragments

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.covary.R
import com.example.covary.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UbahPasswordFragment : Fragment() {

    private var isPasswordVisibleLama = false
    private var isPasswordVisibleBaru = false
    private var isPasswordVisibleKonfirmasi = false
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ubah_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val editTextLama: EditText = view.findViewById(R.id.kataSandiLama)
        val editTextBaru: EditText = view.findViewById(R.id.kataSandiBaru)
        val editTextKonfirmasi: EditText = view.findViewById(R.id.konirmasiKataSandiBaru)

        val btnSimpan: Button = view.findViewById(R.id.btnSimpanUbahKataSandi)

        setupTogglePasswordVisibility(editTextLama) { isVisible -> isPasswordVisibleLama = isVisible }
        setupTogglePasswordVisibility(editTextBaru) { isVisible -> isPasswordVisibleBaru = isVisible }
        setupTogglePasswordVisibility(editTextKonfirmasi) { isVisible -> isPasswordVisibleKonfirmasi = isVisible }

        val backButton: ImageButton = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        btnSimpan.setOnClickListener {
            val passwordLama = editTextLama.text.toString()
            val passwordBaru = editTextBaru.text.toString()
            val konfirmasi = editTextKonfirmasi.text.toString()

            if (passwordBaru != konfirmasi) {
                Toast.makeText(requireContext(), "Kata sandi baru dan konfirmasi kata sandi tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordBaru.isEmpty() || passwordLama.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua kolom kata sandi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(requireContext(), "User tidak ditemukan, silakan masuk ulang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Re-authenticate user dengan password lama sebelum update password baru
            val credential = EmailAuthProvider.getCredential(user.email ?: "", passwordLama)

            user.reauthenticate(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        // Update password baru
                        user.updatePassword(passwordBaru)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(requireContext(), "Kata sandi berhasil diubah, silakan masuk ulang", Toast.LENGTH_LONG).show()
                                    auth.signOut()
                                    googleSignInClient.signOut()

                                    // Arahkan ke activity Login
                                    val intent = Intent(requireContext(), LoginActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish() // hentikan activity sekarang agar tidak bisa back

                                } else {
                                    Toast.makeText(requireContext(), "Gagal mengubah kata sandi", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), "Kata sandi lama salah", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun setupTogglePasswordVisibility(editText: EditText, updateVisibilityFlag: (Boolean) -> Unit) {
        var isPasswordVisible = false

        // Set default drawable awal (password tersembunyi)
        editText.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_lock,
            0,
            R.drawable.ic_hidden_eye, // ikon mata tertutup
            0
        )
        // Pastikan font Montserrat terpasang
        val montserratFont = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)
        editText.typeface = montserratFont

        editText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawableWidth = editText.compoundDrawables[drawableEnd]?.bounds?.width() ?: 0
                // Sesuaikan dengan paddingEnd agar lebih presisi
                if (event.rawX >= (editText.right - editText.paddingEnd - drawableWidth)) {
                    isPasswordVisible = !isPasswordVisible
                    updateVisibilityFlag(isPasswordVisible)

                    if (isPasswordVisible) {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_lock,
                            0,
                            R.drawable.ic_eye, // icon open eye
                            0
                        )
                    } else {
                        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_lock,
                            0,
                            R.drawable.ic_hidden_eye, // icon hidden eye (slash)
                            0
                        )
                    }

                    editText.setSelection(editText.text.length)
                    // Pastikan font tetap Montserrat setelah ubah inputType
                    editText.typeface = montserratFont

                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}