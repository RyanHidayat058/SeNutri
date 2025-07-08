package com.example.covary.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.covary.R
import com.example.covary.assessment.AssessmentFirstActivity
import com.example.covary.databinding.FragmentProfileBinding
import com.example.covary.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadUserProfile()

        binding.btnUbahProfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UbahProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnUbahKataSandi.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UbahPasswordFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnUpdateDataDiri.setOnClickListener {
            val intent = Intent(requireContext(), AssessmentFirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.btnKeluarAkun.setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nama = document.getString("nama") ?: "-"
                        val email = document.getString("email") ?: "-"
                        val tanggalLahir = document.getString("tanggal_lahir") ?: "-"
                        val umur = document.getLong("usia")?.toInt() ?: 0
                        val jenisKelamin = document.getString("jenisKelamin") ?: "-"
                        val profileImageUri = document.getString("profileImageUri")

                        binding.tvNamaLengkap.text = nama
                        binding.tvEmail.text = email
                        binding.tvTanggalLahir.text = tanggalLahir
                        binding.tvUmur.text = "$umur Tahun"
                        binding.tvJenisKelamin.text = jenisKelamin

                        if (!profileImageUri.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(profileImageUri)
                                .placeholder(R.drawable.avatar_user)
                                .error(R.drawable.avatar_user)
                                .into(binding.imgProfile)
                        } else {
                            binding.imgProfile.setImageResource(R.drawable.avatar_user)
                        }

                    } else {
                        Log.d("ProfileFragment", "Dokumen pengguna tidak ditemukan")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileFragment", "Gagal mengambil data pengguna", e)
                }
        } else {
            Log.d("ProfileFragment", "Pengguna belum masuk")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
