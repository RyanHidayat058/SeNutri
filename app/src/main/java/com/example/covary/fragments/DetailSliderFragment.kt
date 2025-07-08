package com.example.covary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.covary.R
class DetailSliderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_slider, container, false)

        val title = arguments?.getString("title")
        val imageResId = arguments?.getInt("imageResId") ?: 0

        val tvTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvDescription = view.findViewById<TextView>(R.id.tvDetailDescription)
        val ivImage = view.findViewById<ImageView>(R.id.ivDetailImage)

        // Tombol back
        val backButton: ImageButton = view.findViewById(R.id.ic_btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val description = when (title) {
            "Tumor/Kanker" -> """
                Definisi/jenis: Tumor ganas adalah pertumbuhan sel abnormal yang tidak terkendali dan bersifat kanker, dapat menyebar serta merusak sel normal di sekitarnya. Penanganan segera oleh dokter, baik dengan atau tanpa operasi, diperlukan.
                
                Pola hidup sehat: Dengan pola hidup sehat seperti makan bergizi, olahraga, cukup istirahat, hindari rokok dan alkohol, serta rutin kontrol ke dokter, Anda mendukung proses penyembuhan. Terus berjuang, harapan kesembuhan selalu ada!
                
                Pengobatan & Harapan Sembuh: Pengobatan tumor ganas dapat dilakukan dengan operasi atau tanpa operasi (kemoterapi, radioterapi, imunoterapi, terapi hormon), tergantung pada jenis, keparahan, kondisi pasien, dan preferensi. Harapan sembuh tergantung pada deteksi dini dan pengobatan yang tepat.
            """.trimIndent()

            "Diabetes" -> """
                Definisi/Jenis: Diabetes adalah kondisi medis di mana kadar gula darah (glukosa) dalam tubuh terlalu tinggi. Terdapat dua jenis utama diabetes: Tipe 1 dan Tipe 2. Penanganan yang tepat melalui gaya hidup sehat dan obat-obatan diperlukan.
                
                Pola Hidup Sehat: Jaga pola makan sehat, olahraga teratur, istirahat cukup, hindari rokok dan alkohol, serta pantau kadar gula darah.
                
                Pengobatan & Harapan Sembuh: Diabetes tidak dapat disembuhkan sepenuhnya, namun dapat dikontrol dengan perubahan gaya hidup, obat, dan insulin.
            """.trimIndent()

            "Hipertensi" -> """
                Definisi/Jenis: Hipertensi atau tekanan darah tinggi bisa menyebabkan kerusakan organ jika tidak dikontrol. Sering disebut "pembunuh diam-diam" karena tidak bergejala.
                
                Pola Hidup Sehat: Kurangi garam, olahraga rutin, jaga berat badan, hindari stres, tidak merokok, dan batasi alkohol. Cek tekanan darah secara berkala.
                
                Pengobatan & Harapan Sembuh: Dapat dikontrol dengan obat dan perubahan gaya hidup meskipun tidak dapat disembuhkan total.
            """.trimIndent()

            "Kolesterol" -> """
                Definisi/Jenis: Kolesterol tinggi, terutama LDL, bisa menyumbat pembuluh darah dan tingkatkan risiko penyakit jantung.
                
                Pola Hidup Sehat: Kurangi lemak jenuh, olahraga teratur, jaga berat badan, tidak merokok, batasi alkohol, dan pantau kadar kolesterol.
                
                Pengobatan & Harapan Sembuh: Pengobatan bisa dengan perubahan gaya hidup dan statin. Dapat dikendalikan untuk mencegah komplikasi.
            """.trimIndent()

            "Asam Urat" -> """
                Definisi/Jenis: Asam urat tinggi bisa membentuk kristal di sendi dan menimbulkan nyeri (gout).
                
                Pola Hidup Sehat: Hindari makanan tinggi purin (daging merah, makanan laut), minuman manis, alkohol, dan jaga berat badan.
                
                Pengobatan & Harapan Sembuh: Kombinasi obat dan gaya hidup sehat bisa mengontrol kadar asam urat dan mencegah serangan lanjutan.
            """.trimIndent()

            else -> {
                tvTitle.text = "Pola Hidup Sehat"
                """
                Pola Hidup Sehat
                Untuk hidup sehat, penting untuk mengonsumsi makanan bergizi, seperti sayur, buah, dan sumber protein yang sehat. Rutin berolahraga, tidur cukup, mengelola stres, tidak merokok, dan membatasi konsumsi alkohol juga sangat penting untuk menjaga kesehatan tubuh dan pikiran.
                
                Manfaat Hidup Sehat
                Hidup sehat memberikan banyak manfaat, termasuk peningkatan energi, pengurangan risiko penyakit kronis, peningkatan mood dan kualitas tidur, serta daya tahan tubuh yang lebih baik terhadap infeksi.
                
                Tips Hidup Sehat
                Untuk hidup sehat, makan dengan seimbang, rutin berolahraga, tidur cukup 7-8 jam setiap malam, kelola stres dengan relaksasi, dan lakukan pemeriksaan kesehatan secara berkala untuk mendeteksi masalah sejak dini.
                """.trimIndent()
            }
        }

        tvTitle.text = title
        tvDescription.text = description
        if (imageResId != 0) {
            ivImage.setImageResource(imageResId)
        }

        return view
    }
}