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
                            "Definisi/jenis: Tumor ganas adalah pertumbuhan sel abnormal yang tidak terkendali dan bersifat kanker, dapat menyebar serta merusak sel normal di sekitarnya. Penanganan segera oleh dokter, baik dengan atau tanpa operasi, diperlukan.\n" +
                                    "\n" +
                                    "Pola hidup sehat: Dengan pola hidup sehat seperti makan bergizi, olahraga, cukup istirahat, hindari rokok dan alkohol, serta rutin kontrol ke dokter, Anda mendukung proses penyembuhan. Terus berjuang, harapan kesembuhan selalu ada!\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan tumor ganas dapat dilakukan dengan operasi atau tanpa operasi (kemoterapi, radioterapi, imunoterapi, terapi hormon), tergantung pada jenis, keparahan, kondisi pasien, dan preferensi. Harapan sembuh tergantung pada deteksi dini dan pengobatan yang tepat.",
                            R.drawable.tumor_foto
                        )
                        "Diabetes" -> SliderItem(
                            "Diabetes",
                            "Definisi/Jenis: Diabetes adalah kondisi medis di mana kadar gula darah (glukosa) dalam tubuh terlalu tinggi. Terdapat dua jenis utama diabetes: Diabetes Tipe 1, di mana tubuh tidak dapat memproduksi insulin, dan Diabetes Tipe 2, di mana tubuh tidak dapat menggunakan insulin secara efektif. Penanganan yang tepat melalui perubahan gaya hidup dan obat-obatan sangat diperlukan untuk mengontrol kadar gula darah.\n" +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola diabetes, penting untuk menjaga pola makan sehat, rutin berolahraga, cukup istirahat, menghindari rokok dan alkohol, serta memantau kadar gula darah secara teratur. Dengan pola hidup yang tepat, Anda dapat menjaga kesehatan dan mencegah komplikasi.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan diabetes dapat dilakukan dengan perubahan gaya hidup, obat-obatan, dan insulin (untuk tipe 1). Harapan sembuh bergantung pada pengelolaan yang tepat dan pemantauan rutin, meskipun diabetes tidak dapat disembuhkan sepenuhnya, namun dapat dikendalikan dengan baik untuk hidup yang sehat.",
                            R.drawable.diabetes_foto
                        )
                        "Hipertensi" -> SliderItem(
                            "Hipertensi",
                            "Definisi/Jenis: Hipertensi, atau tekanan darah tinggi, adalah kondisi di mana tekanan darah dalam arteri meningkat secara konsisten. Jika tidak dikendalikan, hipertensi dapat menyebabkan kerusakan pada jantung, pembuluh darah, ginjal, dan organ tubuh lainnya. Hipertensi sering kali disebut \"pembunuh diam-diam\" karena gejalanya yang sering tidak terlihat.\n" +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola hipertensi, penting untuk menjaga pola makan rendah garam, rutin berolahraga, menjaga berat badan ideal, menghindari stres, serta tidak merokok dan membatasi konsumsi alkohol. Pemantauan tekanan darah secara teratur juga sangat penting untuk mencegah komplikasi.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan hipertensi biasanya melibatkan perubahan gaya hidup dan penggunaan obat-obatan untuk menurunkan tekanan darah. Dengan pengelolaan yang tepat dan pengobatan yang sesuai, hipertensi dapat dikendalikan, meskipun tidak bisa disembuhkan sepenuhnya, namun dapat mengurangi risiko komplikasi serius.",
                            R.drawable.hipertensi_foto
                        )
                        "Kolesterol" -> SliderItem(
                            "Kolesterol",
                            "Definisi/Jenis: Kolesterol adalah lemak yang ditemukan dalam darah yang dibutuhkan tubuh untuk berfungsi dengan baik. Namun, kadar kolesterol yang tinggi, terutama kolesterol LDL (kolesterol jahat), dapat menyebabkan penumpukan plak di dinding pembuluh darah, meningkatkan risiko penyakit jantung dan stroke. Terdapat dua jenis utama kolesterol: HDL (kolesterol baik) dan LDL (kolesterol jahat).\n" +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola kadar kolesterol, penting untuk menjaga pola makan rendah lemak jenuh, rutin berolahraga, menjaga berat badan ideal, tidak merokok, dan membatasi konsumsi alkohol. Pemantauan kadar kolesterol secara rutin juga penting untuk menjaga kesehatan jantung.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan kolesterol tinggi melibatkan perubahan gaya hidup dan, jika diperlukan, penggunaan obat-obatan seperti statin untuk menurunkan kadar kolesterol LDL. Dengan pengelolaan yang tepat, kolesterol dapat dikendalikan, mengurangi risiko penyakit jantung dan komplikasi lainnya.",
                            R.drawable.kolesterol_foto
                        )
                        "Asam Urat" -> SliderItem(
                            "Asam Urat",
                            "Definisi/Jenis: Asam urat adalah zat yang terbentuk ketika tubuh memecah purin, yang ditemukan dalam makanan tertentu dan sel tubuh. Kadar asam urat yang tinggi dapat menyebabkan kristal terbentuk di persendian, menyebabkan nyeri dan peradangan, kondisi yang dikenal sebagai gout (radang sendi akibat asam urat).\n" +
                                    "\n" +
                                    "Pola Hidup Sehat: Untuk mengelola asam urat, penting untuk menjaga pola makan rendah purin, menghindari alkohol, menjaga berat badan ideal, rutin berolahraga, dan cukup minum air putih. Menghindari makanan tinggi purin seperti daging merah, makanan laut, dan minuman manis juga dapat membantu.\n" +
                                    "\n" +
                                    "Pengobatan & Harapan Sembuh: Pengobatan asam urat meliputi perubahan pola makan, konsumsi obat-obatan untuk menurunkan kadar asam urat, serta mengatasi peradangan dan nyeri. Dengan pengelolaan yang tepat, asam urat dapat dikendalikan, mengurangi serangan gout dan mencegah komplikasi lebih lanjut.",
                            R.drawable.asamurat_foto
                        )
                        else -> SliderItem(
                            "Jaga Kesehatan Tubuh",
                            "Untuk hidup sehat, penting untuk mengonsumsi makanan bergizi, seperti sayur, buah, dan sumber protein yang sehat. Rutin berolahraga, tidur cukup, mengelola stres, tidak merokok, dan membatasi konsumsi alkohol juga sangat penting untuk menjaga kesehatan tubuh dan pikiran.",
                            R.drawable.hidupsehat_foto
                        )
                    }
                }
            }
            recyclerView.adapter = HomeAdapter(requireContext(), sliderItems, this, this)
        }
    }
}