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
                Definisi/Jenis: 
                Tumor ganas adalah pertumbuhan sel abnormal yang tidak terkendali dan bersifat kanker. Tumor ini dapat menyebar ke bagian tubuh lain (metastasis) dan merusak jaringan serta organ tubuh yang sehat. Penanganan tumor ganas melibatkan berbagai metode, termasuk operasi, kemoterapi, radioterapi, dan imunoterapi, tergantung pada jenis kanker dan stadium penyakit. Kanker dapat menyebar melalui darah dan sistem limfatik, menyebabkan metastasis ke organ tubuh lainnya, yang membuat pengobatan semakin kompleks (American Cancer Society, 2023)
                
                Pola Hidup Sehat: 
                Pola hidup sehat yang mendukung pencegahan dan pemulihan kanker meliputi diet bergizi, rutin berolahraga, tidur yang cukup, tidak merokok, menghindari alkohol, serta kontrol kesehatan rutin. Sebuah pola hidup sehat membantu meningkatkan sistem kekebalan tubuh dan mengurangi risiko terkena kanker.
                
                Pengobatan & Harapan Sembuh: 
                Pengobatan tumor ganas dapat berupa operasi atau terapi lainnya seperti kemoterapi, radioterapi, atau imunoterapi. Harapan sembuh bergantung pada deteksi dini, pengobatan yang tepat, serta kondisi umum pasien. Prognosis bervariasi berdasarkan jenis kanker dan keparahannya. Deteksi dini adalah kunci untuk meningkatkan harapan sembuh bagi pasien kanker.
                
                Sumber: American Cancer Society. (2023). "Cancer Facts & Figures." Cancer.org
            """.trimIndent()

            "Diabetes" -> """
                Definisi/Jenis: 
                Diabetes adalah gangguan metabolik yang terjadi ketika tubuh tidak dapat memproduksi insulin (diabetes tipe 1) atau tidak dapat menggunakan insulin secara efektif (diabetes tipe 2). Diabetes tipe 1 biasanya didiagnosis pada usia muda, sementara diabetes tipe 2 lebih sering berkembang pada orang dewasa akibat pola hidup yang tidak sehat. Diabetes yang tidak dikelola dengan baik dapat menyebabkan komplikasi serius, termasuk kerusakan pada ginjal, saraf, dan jantung. Diabetes tipe 2 sering dapat dikendalikan melalui perubahan gaya hidup, namun diabetes tipe 1 memerlukan penggunaan insulin seumur hidup (National Institute of Diabetes and Digestive and Kidney Diseases [NIDDK], 2022).
                
                Pola Hidup Sehat: 
                Untuk mengelola diabetes, sangat penting untuk menjaga pola makan sehat, berolahraga secara rutin, menghindari stres, tidur yang cukup, serta memantau kadar gula darah secara teratur.
                
                Pengobatan & Harapan Sembuh: 
                Diabetes tipe 1 memerlukan pengobatan insulin seumur hidup. Diabetes tipe 2 dapat dikelola dengan perubahan gaya hidup, obat-obatan, dan kadang-kadang insulin. Meskipun diabetes tidak dapat disembuhkan sepenuhnya, dengan pengelolaan yang baik, pasien dapat hidup sehat dan terhindar dari komplikasi.
                
                Sumber: National Institute of Diabetes and Digestive and Kidney Diseases (NIDDK). (2022). "Diabetes Overview." NIDDK.nih.gov
            """.trimIndent()

            "Hipertensi" -> """
                Definisi/Jenis: 
                Hipertensi adalah kondisi medis di mana tekanan darah dalam pembuluh darah meningkat secara terus-menerus. Tekanan darah tinggi dapat merusak pembuluh darah dan jantung, serta meningkatkan risiko stroke, serangan jantung, dan gagal ginjal. Hipertensi sering disebut "silent killer" karena biasanya tidak menunjukkan gejala sampai kerusakan yang signifikan terjadi. Hipertensi dapat menyebabkan kerusakan serius pada pembuluh darah dan organ vital jika tidak segera ditangani (World Health Organization [WHO], 2023).
                
                Pola Hidup Sehat: 
                Mengelola hipertensi membutuhkan pola makan rendah garam, rutin berolahraga, menjaga berat badan ideal, menghindari stres, tidak merokok, serta memantau tekanan darah secara teratur. Ini dapat mengurangi risiko komplikasi yang lebih serius.
                
                Pengobatan & Harapan Sembuh: 
                Pengobatan hipertensi meliputi perubahan gaya hidup dan penggunaan obat-obatan antihipertensi. Walaupun hipertensi tidak dapat disembuhkan sepenuhnya, dengan pengelolaan yang tepat, tekanan darah dapat dikendalikan dengan baik.
                
                Sumber: World Health Organization (WHO). (2023). "Hypertension." WHO.int
            """.trimIndent()

            "Kolesterol" -> """
                Definisi/Jenis: 
                Kolesterol adalah jenis lemak yang ditemukan dalam darah yang diperlukan tubuh untuk membangun sel-sel sehat. Namun, kolesterol tinggi, terutama kolesterol LDL (kolesterol jahat), dapat menyebabkan penumpukan plak dalam pembuluh darah, yang meningkatkan risiko penyakit jantung dan stroke. Kolesterol HDL (kolesterol baik) membantu mengurangi risiko ini dengan mengangkut kolesterol jahat kembali ke hati. Tingginya kadar kolesterol LDL dapat menyebabkan aterosklerosis, yaitu penumpukan plak yang menyempitkan pembuluh darah (Mayo Clinic, 2023).
                
                Pola Hidup Sehat: 
                Untuk menjaga kadar kolesterol yang sehat, penting untuk makan makanan rendah lemak jenuh, rutin berolahraga, tidak merokok, dan membatasi konsumsi alkohol. Pemantauan kadar kolesterol secara rutin juga sangat penting.
                
                Pengobatan & Harapan Sembuh: 
                Kolesterol tinggi dapat dikendalikan dengan perubahan gaya hidup dan, jika diperlukan, penggunaan obat-obatan seperti statin. Dengan pengelolaan yang tepat, kolesterol dapat dikendalikan dan risiko penyakit jantung serta stroke dapat berkurang.
                
                Sumber: Mayo Clinic. (2023). "Cholesterol Levels." MayoClinic.org
            """.trimIndent()

            "Asam Urat" -> """
                Definisi/Jenis: 
                Asam urat terbentuk ketika tubuh memecah purin, yang ditemukan dalam makanan dan sel tubuh. Jika kadar asam urat terlalu tinggi, kristal dapat terbentuk di persendian, menyebabkan nyeri hebat dan peradangan, yang dikenal sebagai gout atau radang sendi akibat asam urat. Penyakit gout sering menyebabkan serangan mendalam pada sendi-sendi besar, seperti ibu jari kaki, yang menimbulkan rasa sakit yang parah (Cleveland Clinic, 2023).
                
                Pola Hidup Sehat: 
                Untuk mengelola asam urat, penting untuk menjaga pola makan rendah purin, menghindari alkohol, rutin berolahraga, dan cukup minum air putih. Makanan seperti daging merah dan makanan laut harus dibatasi.
                
                Pengobatan & Harapan Sembuh: 
                Pengobatan untuk asam urat termasuk perubahan pola makan, obat-obatan untuk menurunkan kadar asam urat, serta pengobatan untuk mengatasi peradangan dan nyeri. Asam urat dapat dikendalikan dengan pengobatan yang tepat.
                
                Sumber: Cleveland Clinic. (2023). "Gout." https://my.clevelandclinic.org/
            """.trimIndent()

            else -> {
                tvTitle.text = "Pola Hidup Sehat Secara Umum"
                """
                Manfaat Hidup Sehat:
                Pola hidup sehat membantu meningkatkan energi, memperbaiki kualitas tidur, mengurangi risiko penyakit kronis, dan memperkuat sistem imun tubuh. Dengan makan makanan bergizi, berolahraga teratur, tidur cukup, serta mengelola stres, kita dapat menjaga kesehatan tubuh dan pikiran secara optimal.
                
                Tips Hidup Sehat:
                •	Makan dengan seimbang, termasuk sayur, buah, dan sumber protein sehat.
                •	Rutin berolahraga (setidaknya 150 menit per minggu).
                •	Tidur cukup 7-8 jam setiap malam.
                •	Kelola stres dengan relaksasi dan meditasi.
                •	Lakukan pemeriksaan kesehatan secara berkala.

                Sumber: World Health Organization (WHO). (2022). "Healthy Diet." WHO.int
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