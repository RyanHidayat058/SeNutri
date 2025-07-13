package com.example.covary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R
import com.example.covary.slider.HomeAdapter
import com.example.covary.slider.SliderItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment(), HomeAdapter.OnRekomendasiClickListener, HomeAdapter.OnRekomendasiAktivitasClickListener{

    private lateinit var recyclerView: RecyclerView
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadPenyakitUser()

        return view
    }

    override fun onRekomendasiClicked(data: Bundle) {
        // data berisi semua info slider: title, description, imageResId

        val detailFragment = DetailSliderFragment() // sesuaikan nama fragment detailnya
        detailFragment.arguments = data

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onRekomendasiAktivitasClicked(saranAktivitas: String) {
        val bundle = Bundle()
        bundle.putString("saran_aktivitas", saranAktivitas)

        val detailFragment = DetailRekomendasiAktivitasFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)  // Ganti dengan container id yang sesuai di layout
            .addToBackStack(null)
            .commit()
    }

    private fun loadPenyakitUser() {
        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            val penyakitStr = document.getString("penyakit") ?: ""
            val penyakitList = penyakitStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            val sliderItems = if (penyakitList.isEmpty()) {
                listOf(
                    SliderItem(
                        "Jaga Kesehatan Tubuh",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia odio vitae vestibulum vestibulum.",
                        R.drawable.hidupsehat_foto
                    )
                )
            } else {
                penyakitList.map { penyakit ->
                    when (penyakit) {
                        "Tumor/Kanker" -> SliderItem(
                            "Tumor/Kanker",
                            "Tumor ganas adalah pertumbuhan sel abnormal yang tidak terkendali dan bersifat kanker. Tumor ini dapat menyebar ke bagian tubuh lain (metastasis) dan merusak jaringan serta organ tubuh yang sehat. Penanganan tumor ganas melibatkan berbagai metode, termasuk operasi, kemoterapi, radioterapi, dan imunoterapi, tergantung pada jenis kanker dan stadium penyakit. Kanker dapat menyebar melalui darah dan sistem limfatik, menyebabkan metastasis ke organ tubuh lainnya, yang membuat pengobatan semakin kompleks (American Cancer Society, 2023)" +
                                    "\n" +
                                    "Pola hidup sehat: Dengan pola hidup sehat seperti makan bergizi, olahraga, cukup istirahat, hindari rokok dan alkohol, serta rutin kontrol ke dokter, Anda mendukung proses penyembuhan. Terus berjuang, harapan kesembuhan selalu ada!\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan tumor ganas dapat dilakukan dengan operasi atau tanpa operasi (kemoterapi, radioterapi, imunoterapi, terapi hormon), tergantung pada jenis, keparahan, kondisi pasien, dan preferensi. Harapan sembuh tergantung pada deteksi dini dan pengobatan yang tepat.",
                            R.drawable.tumor_foto
                        )
                        "Diabetes" -> SliderItem(
                            "Diabetes",
                            "Diabetes adalah gangguan metabolik yang terjadi ketika tubuh tidak dapat memproduksi insulin (diabetes tipe 1) atau tidak dapat menggunakan insulin secara efektif (diabetes tipe 2). Diabetes tipe 1 biasanya didiagnosis pada usia muda, sementara diabetes tipe 2 lebih sering berkembang pada orang dewasa akibat pola hidup yang tidak sehat. Diabetes yang tidak dikelola dengan baik dapat menyebabkan komplikasi serius, termasuk kerusakan pada ginjal, saraf, dan jantung. Diabetes tipe 2 sering dapat dikendalikan melalui perubahan gaya hidup, namun diabetes tipe 1 memerlukan penggunaan insulin seumur hidup (National Institute of Diabetes and Digestive and Kidney Diseases [NIDDK], 2022)." +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola diabetes, penting untuk menjaga pola makan sehat, rutin berolahraga, cukup istirahat, menghindari rokok dan alkohol, serta memantau kadar gula darah secara teratur. Dengan pola hidup yang tepat, Anda dapat menjaga kesehatan dan mencegah komplikasi.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan diabetes dapat dilakukan dengan perubahan gaya hidup, obat-obatan, dan insulin (untuk tipe 1). Harapan sembuh bergantung pada pengelolaan yang tepat dan pemantauan rutin, meskipun diabetes tidak dapat disembuhkan sepenuhnya, namun dapat dikendalikan dengan baik untuk hidup yang sehat.",
                            R.drawable.diabetes_foto
                        )
                        "Hipertensi" -> SliderItem(
                            "Hipertensi",
                            "Hipertensi adalah kondisi medis di mana tekanan darah dalam pembuluh darah meningkat secara terus-menerus. Tekanan darah tinggi dapat merusak pembuluh darah dan jantung, serta meningkatkan risiko stroke, serangan jantung, dan gagal ginjal. Hipertensi sering disebut \"silent killer\" karena biasanya tidak menunjukkan gejala sampai kerusakan yang signifikan terjadi. Hipertensi dapat menyebabkan kerusakan serius pada pembuluh darah dan organ vital jika tidak segera ditangani (World Health Organization [WHO], 2023)." +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola hipertensi, penting untuk menjaga pola makan rendah garam, rutin berolahraga, menjaga berat badan ideal, menghindari stres, serta tidak merokok dan membatasi konsumsi alkohol. Pemantauan tekanan darah secara teratur juga sangat penting untuk mencegah komplikasi.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan hipertensi biasanya melibatkan perubahan gaya hidup dan penggunaan obat-obatan untuk menurunkan tekanan darah. Dengan pengelolaan yang tepat dan pengobatan yang sesuai, hipertensi dapat dikendalikan, meskipun tidak bisa disembuhkan sepenuhnya, namun dapat mengurangi risiko komplikasi serius.",
                            R.drawable.hipertensi_foto
                        )
                        "Kolesterol" -> SliderItem(
                            "Kolesterol",
                            "Kolesterol adalah jenis lemak yang ditemukan dalam darah yang diperlukan tubuh untuk membangun sel-sel sehat. Namun, kolesterol tinggi, terutama kolesterol LDL (kolesterol jahat), dapat menyebabkan penumpukan plak dalam pembuluh darah, yang meningkatkan risiko penyakit jantung dan stroke. Kolesterol HDL (kolesterol baik) membantu mengurangi risiko ini dengan mengangkut kolesterol jahat kembali ke hati. Tingginya kadar kolesterol LDL dapat menyebabkan aterosklerosis, yaitu penumpukan plak yang menyempitkan pembuluh darah (Mayo Clinic, 2023)." +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola kadar kolesterol, penting untuk menjaga pola makan rendah lemak jenuh, rutin berolahraga, menjaga berat badan ideal, tidak merokok, dan membatasi konsumsi alkohol. Pemantauan kadar kolesterol secara rutin juga penting untuk menjaga kesehatan jantung.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan kolesterol tinggi melibatkan perubahan gaya hidup dan, jika diperlukan, penggunaan obat-obatan seperti statin untuk menurunkan kadar kolesterol LDL. Dengan pengelolaan yang tepat, kolesterol dapat dikendalikan, mengurangi risiko penyakit jantung dan komplikasi lainnya.",
                            R.drawable.kolesterol_foto
                        )
                        "Asam Urat" -> SliderItem(
                            "Asam Urat",
                            "Asam urat terbentuk ketika tubuh memecah purin, yang ditemukan dalam makanan dan sel tubuh. Jika kadar asam urat terlalu tinggi, kristal dapat terbentuk di persendian, menyebabkan nyeri hebat dan peradangan, yang dikenal sebagai gout atau radang sendi akibat asam urat. Penyakit gout sering menyebabkan serangan mendalam pada sendi-sendi besar, seperti ibu jari kaki, yang menimbulkan rasa sakit yang parah (Cleveland Clinic, 2023)." +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola asam urat, penting untuk menjaga pola makan rendah purin, menghindari alkohol, menjaga berat badan ideal, rutin berolahraga, dan cukup minum air putih. Menghindari makanan tinggi purin seperti daging merah, makanan laut, dan minuman manis juga dapat membantu.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan asam urat meliputi perubahan pola makan, konsumsi obat-obatan untuk menurunkan kadar asam urat, serta mengatasi peradangan dan nyeri. Dengan pengelolaan yang tepat, asam urat dapat dikendalikan, mengurangi serangan gout dan mencegah komplikasi lebih lanjut.",
                            R.drawable.asamurat_foto
                        )
                        else -> SliderItem(
                            "Pola Hidup Sehat Secara Umum",
                            "Pola hidup sehat membantu meningkatkan energi, memperbaiki kualitas tidur, mengurangi risiko penyakit kronis, dan memperkuat sistem imun tubuh. Dengan makan makanan bergizi, berolahraga teratur, tidur cukup, serta mengelola stres, kita dapat menjaga kesehatan tubuh dan pikiran secara optimal.",
                            R.drawable.hidupsehat_foto
                        )
                    }
                }
            }
            recyclerView.adapter = HomeAdapter(requireContext(), sliderItems, this, this)
        }
    }
}