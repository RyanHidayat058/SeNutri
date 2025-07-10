package com.example.covary.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.covary.R
import com.example.covary.imgurapi.ImgurApiService
import com.example.covary.imgurapi.ImgurResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class UbahProfileFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var imgProfile: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ubah_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val edtNama = view.findViewById<EditText>(R.id.namaKamu)
        val datePickerLayout = view.findViewById<RelativeLayout>(R.id.datePickerLayout)
        val tvTanggalLahir = view.findViewById<TextView>(R.id.tvTanggalLahir)
        val genderSpinner = view.findViewById<Spinner>(R.id.spinner_gender)
        val btnKonfirmasi = view.findViewById<Button>(R.id.btnKonfirmasiUbahKataSandi)
        val imgEditIcon: ImageView = view.findViewById(R.id.imgEditIcon)
        imgProfile = view.findViewById(R.id.imgProfile)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val imageUrl = document.getString("profileImageUri")
                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.avatar_user)
                                .into(imgProfile)
                        } else {
                            imgProfile.setImageResource(R.drawable.avatar_user)
                        }
                    } else {
                        imgProfile.setImageResource(R.drawable.avatar_user)
                    }
                }
                .addOnFailureListener {
                    imgProfile.setImageResource(R.drawable.avatar_user)
                }
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        datePickerLayout.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                tvTanggalLahir.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        val genderOptions = arrayOf("Pilih Jenis Kelamin", "Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderOptions)
        genderSpinner.adapter = adapter

        imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Tombol back
        val backButton: ImageButton = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        btnKonfirmasi.setOnClickListener {
            val newNama = edtNama.text.toString().trim()
            val newTanggalLahir = tvTanggalLahir.text.toString().trim()
            val newJenisKelamin = genderSpinner.selectedItem.toString()
            val updateData = hashMapOf<String, Any>()

            if (newNama.isNotEmpty()) updateData["nama"] = newNama
            if (newTanggalLahir.isNotEmpty() && newTanggalLahir != "-" && newTanggalLahir != "Masukan tanggal lahir kamu") {
                updateData["tanggal_lahir"] = newTanggalLahir

                try {
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val birthDate = sdf.parse(newTanggalLahir)
                    val today = Calendar.getInstance()
                    val dob = Calendar.getInstance()
                    dob.time = birthDate

                    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
                    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                        age--
                    }

                    if (age >= 0) updateData["usia"] = age
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (newJenisKelamin != "Pilih Jenis Kelamin") updateData["jenisKelamin"] = newJenisKelamin

            // Tampilkan loading
            progressBar.visibility = View.VISIBLE
            btnKonfirmasi.isEnabled = false

            // Delay 4 detik lalu update data
            view.postDelayed({
                if (imageUri != null) {
                    uploadToImgur(imageUri!!,
                        onSuccess = { imageUrl ->
                            updateData["profileImageUri"] = imageUrl
                            updateFirestoreProfile(firestore, auth, updateData)
                        },
                        onError = {
                            progressBar.visibility = View.GONE
                            btnKonfirmasi.isEnabled = true
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    updateFirestoreProfile(firestore, auth, updateData)
                }
            }, 2000)
        }
    }

    private fun uploadToImgur(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ImgurApiService::class.java)

        try {
            // Buka inputStream lalu ubah ke Bitmap
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) {
                onError("Gagal membaca gambar.")
                return
            }

            // Kompres bitmap jadi JPEG di memory
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("image", "upload.jpg", requestBody)
            val clientId = "fd5cbadd2491d35"
            val call = service.uploadImage("Client-ID $clientId", multipart)

            call.enqueue(object : Callback<ImgurResponse> {
                override fun onResponse(call: Call<ImgurResponse>, response: Response<ImgurResponse>) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.data?.link
                        if (!imageUrl.isNullOrEmpty()) {
                            onSuccess(imageUrl)
                        } else {
                            onError("Upload berhasil, tapi URL kosong.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("IMGUR_ERROR", errorBody ?: "Unknown error")
                        onError("Upload gagal (${response.code()}): $errorBody")
                    }
                }

                override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
                    onError("Gagal upload ke Imgur: ${t.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onError("Terjadi kesalahan saat membaca gambar: ${e.message}")
        }
    }

    private fun updateFirestoreProfile(firestore: FirebaseFirestore, auth: FirebaseAuth, updateData: Map<String, Any>) {
        firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .update(updateData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                view?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
                view?.findViewById<Button>(R.id.btnKonfirmasiUbahKataSandi)?.isEnabled = true

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProfileFragment())
                    .commit()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Profil gagal diperbarui", Toast.LENGTH_SHORT).show()
                view?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
                view?.findViewById<Button>(R.id.btnKonfirmasiUbahKataSandi)?.isEnabled = true
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            // Simpan izin akses URI
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    imageUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

            imgProfile.setImageURI(imageUri)
        }
    }
}
