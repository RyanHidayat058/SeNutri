package com.example.covary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.example.covary.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qamar.curvedbottomnaviagtion.visible

class DetailRekomendasiAktivitasFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_rekomendasi_aktivitas, container, false)

        val backButton: ImageButton = view.findViewById(R.id.ic_btnBack)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val tvDetail: TextView = view.findViewById(R.id.tvDetailSaran)
        val tvsubjudul: TextView = view.findViewById(R.id.tvsubjudul)
        val tvnomor1: TextView = view.findViewById(R.id.tvnomor1)
        val tvnomor2: TextView = view.findViewById(R.id.tvnomor2)
        val tvnomor3: TextView = view.findViewById(R.id.tvnomor3)
        val tvnomor4: TextView = view.findViewById(R.id.tvnomor4)
        val tvnomor5: TextView = view.findViewById(R.id.tvnomor5)
        val tvtambahan: TextView = view.findViewById(R.id.tvtambahan)

        val img1: ImageView = view.findViewById(R.id.img1)
        val img2: ImageView = view.findViewById(R.id.img2)
        val img3: ImageView = view.findViewById(R.id.img3)
        val img4: ImageView = view.findViewById(R.id.img4)
        val img5: ImageView = view.findViewById(R.id.img5)

        val saran = arguments?.getString("saran_aktivitas") ?: "Data tidak tersedia"

        // Ambil data kategoriIMT dari Firestore
        if (userId.isNotEmpty()) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val kategoriIMT = document.getString("kategoriIMT") ?: "Unknown"

                    if (kategoriIMT == "Obesitas") {
                        tvsubjudul.text = "Jenis olahraga yang disarankan:"
                        tvnomor5.isGone = true
                        tvtambahan.isGone = true
                        img1.setImageResource(R.drawable.img_cardio)
                        img2.setImageResource(R.drawable.img_membangun_otot)
                        img3.setImageResource(R.drawable.img_aktivitasringan)
                        img4.setImageResource(R.drawable.img_yoga)
                        img5.isGone = true

                        tvnomor1.text = """
    |1. Latihan kardio berdampak rendah
    |   Cocok untuk persendian namun tetap efektif:
    |   • Berjalan kaki (dapat dimulai selama 10–15 menit, lalu tingkatkan durasinya)
    |   • Bersepeda statis atau bersepeda biasa
    |   • Berenang atau aerobik air (lebih ringan bagi persendian)
    |   • Senam aerobik ringan (dapat mengikuti video pemula di rumah)
""".trimMargin()

                        tvnomor2.text = """
    |2. Latihan kekuatan ringan (2–3 kali per minggu)
    |   Bertujuan membentuk otot dan mendukung penurunan berat badan:
    |   • Gerakan duduk dan berdiri dari kursi (squat dengan kursi)
    |   • Mengangkat botol air sebagai beban
    |   • Push-up dengan bertumpu pada meja atau dinding
""".trimMargin()

                        tvnomor3.text = """
    |3. Aktivitas fungsional harian
    |   • Naik tangga secara perlahan
    |   • Berjalan kaki di dalam rumah sambil mendengarkan musik
    |   • Membersihkan rumah atau berkebun
""".trimMargin()

                        tvnomor4.text = """
    |4. Peregangan dan latihan pernapasan
    |   • Yoga untuk pemula atau senam pernapasan
    |   • Membantu relaksasi, memperbaiki postur tubuh, dan mengurangi stres
    |
    |Durasi awal yang disarankan:
    |   • Mulailah dari 10–15 menit per hari, lalu tingkatkan secara bertahap
    |   • Targetkan 150–300 menit aktivitas fisik per minggu dalam jangka panjang
""".trimMargin()

                        tvDetail.text = saran
                    } else if (kategoriIMT == "Underweight") {
                        tvDetail.text = saran
                        tvsubjudul.text = "Jenis olahraga yang disarankan:"
                        tvnomor4.isGone = true
                        tvnomor5.isGone = true
                        tvtambahan.isGone = true
                        img1.setImageResource(R.drawable.img_membangun_otot)
                        img2.setImageResource(R.drawable.img_yoga)
                        img3.setImageResource(R.drawable.img_cardio)
                        img4.isGone = true
                        img5.isGone = true

                        tvnomor1.text = """
    |1. Latihan kekuatan (Strength Training)
    |   Bertujuan membentuk otot dan menambah berat badan melalui peningkatan massa otot, bukan lemak:
    |   • Mengangkat beban ringan hingga sedang
    |   • Latihan berat badan: push-up, squat, pull-up, plank
    |   • Latihan menggunakan resistance band
    |   • Latihan ini sebaiknya dilakukan 2–4 kali per minggu
""".trimMargin()

                        tvnomor2.text = """
    |2. Yoga atau Pilates
    |   • Membantu memperbaiki postur tubuh, menenangkan pikiran, dan mengurangi stres
    |   • Yoga juga dapat merangsang hormon lapar dan menyeimbangkan sistem tubuh
""".trimMargin()

                        tvnomor3.text = """
    |3. Kardio ringan (tidak berlebihan)
    |   • Berjalan kaki santai, bersepeda ringan, atau berenang secara santai
    |   • Dilakukan hanya 1–2 kali per minggu agar tidak membakar terlalu banyak kalori
""".trimMargin()

                    } else if (kategoriIMT == "Normal") {
                        tvDetail.text = saran
                        tvsubjudul.text = "Jenis olahraga yang disarankan:"
                        tvnomor4.isGone = true
                        tvnomor5.isGone = true
                        tvtambahan.isGone = true
                        img1.setImageResource(R.drawable.img_cardio)
                        img2.setImageResource(R.drawable.img_membangun_otot)
                        img3.isGone = true
                        img4.isGone = true
                        img5.isGone = true

                        tvnomor1.text = """
    |1. Kardio ringan hingga sedang
    |   Bertujuan menjaga kesehatan jantung dan metabolisme:
    |   • Berjalan cepat (30 menit, 5 kali per minggu)
    |   • Jogging ringan
    |   • Bersepeda
    |   • Berenang
""".trimMargin()

                        tvnomor2.text = """
    |2. Latihan kekuatan (Strength Training)
    |   Untuk mempertahankan massa otot dan menjaga metabolisme:
    |   • Mengangkat beban ringan
    |   • Latihan berat badan seperti push-up, squat, dan plank
    |   • Dilakukan 2–3 kali per minggu
""".trimMargin()

                        tvnomor3.text = """
    |Durasi ideal:
    |   • 150 menit olahraga dengan intensitas sedang per minggu
    |   atau
    |   • 75 menit olahraga dengan intensitas tinggi per minggu
""".trimMargin()

                    } else if (kategoriIMT == "Overweight") {
                        tvDetail.text = saran
                        tvsubjudul.text = "Jenis olahraga yang disarankan:"
                        tvnomor5.isGone = true
                        tvtambahan.isGone = true
                        img1.setImageResource(R.drawable.img_cardio)
                        img2.setImageResource(R.drawable.img_membangun_otot)
                        img3.setImageResource(R.drawable.img_aktivitasringan)
                        img4.setImageResource(R.drawable.img_hobi)
                        img5.isGone = true

                        tvnomor1.text = """
    |1. Kardio (utama untuk membakar lemak)
    |   Lakukan minimal 150–300 menit per minggu:
    |   • Berjalan cepat (paling aman dan mudah)
    |   • Bersepeda statis atau di luar ruangan
    |   • Berenang (beban rendah pada persendian)
    |   • Senam aerobik atau zumba untuk pemula
""".trimMargin()

                        tvnomor2.text = """
    |2. Latihan kekuatan (2–3 kali per minggu)
    |   Untuk mempertahankan otot selama penurunan berat badan:
    |   • Mengangkat beban ringan atau menggunakan resistance band
    |   • Latihan berat badan seperti squat, wall sit, dan push-up di dinding
""".trimMargin()

                        tvnomor3.text = """
    |3. Aktivitas ringan sehari-hari (non-olahraga)
    |   • Naik turun tangga
    |   • Membersihkan rumah
    |   • Berjalan kaki setiap 1 jam jika duduk terlalu lama
""".trimMargin()

                        tvnomor4.text = """
    |4. Olahraga berbasis hobi
    |   Agar lebih konsisten:
    |   • Bulu tangkis santai, menari, atau berkebun
""".trimMargin()

                    } else {
                        tvDetail.text = "$saran\n\nKategori IMT tidak diketahui."
                    }
                }
                .addOnFailureListener {
                    // Jika gagal ambil data, tampilkan pesan error sederhana
                    tvDetail.text = "$saran\n\nGagal mengambil data kategori IMT."
                }
        } else {
            // Jika userId kosong, langsung tampilkan dengan pesan default
            tvDetail.text = "$saran\n\nTidak ada user login."
        }

        return view
    }
}