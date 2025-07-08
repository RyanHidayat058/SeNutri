package com.example.covary.slider

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.covary.R
import com.example.covary.worker.SaveVolumeWorker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class HomeAdapter(
    private val context: Context,
    private val sliderItems: List<SliderItem>,
    private val rekomendasiClickListener: OnRekomendasiClickListener,
    private val aktivitasClickListener: OnRekomendasiAktivitasClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SLIDER = 0
    private val TYPE_REKOMENDASI = 1

    // Data volume cairan dalam ml (misal mulai 250 ml)
    private var currentVolumeMl = 0

    override fun getItemCount() = 2 // 1 slider + 1 rekomendasi minum

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_SLIDER else TYPE_REKOMENDASI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SLIDER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
            SliderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_other, parent, false)
            RekomendasiViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SliderViewHolder) {
            holder.bind(sliderItems)
        } else if (holder is RekomendasiViewHolder) {
            holder.bind(currentVolumeMl)
        }
    }

    private fun setupDailySaveWorker(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<SaveVolumeWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelayToMidnight(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SaveVolumeDaily",
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyWorkRequest
        )
    }

    private fun calculateInitialDelayToMidnight(): Long {
        val now = Calendar.getInstance()
        val nextMidnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return nextMidnight.timeInMillis - now.timeInMillis
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SliderAdapter.OnSliderItemClickListener {

        private val viewPager: ViewPager2 = itemView.findViewById(R.id.viewPagerSlider)
        private val tabLayout: TabLayout = itemView.findViewById(R.id.tabLayoutIndicator)

        fun bind(items: List<SliderItem>) {
            val sliderAdapter = SliderAdapter(items, this)
            viewPager.adapter = sliderAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
                tab.text = ""
                tab.setCustomView(R.layout.custom_tab_dot)
            }.attach()
        }

        override fun onSliderItemClicked(data: Bundle) {
            rekomendasiClickListener.onRekomendasiClicked(data)
        }
    }

    interface OnRekomendasiClickListener {
        fun onRekomendasiClicked(data: Bundle)
    }

    interface OnRekomendasiAktivitasClickListener {
        fun onRekomendasiAktivitasClicked(saranAktivitas: String)
    }

    inner class RekomendasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvVolume: TextView = itemView.findViewById(R.id.tvVolume)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        private val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        private val llRekomendasiAktivitas: View = itemView.findViewById(R.id.llRekomendasiAktivitas)

        private val tvSaranAktivitas: TextView = itemView.findViewById(R.id.tvsaranAktivitas)
        private val TinggiBadanUser: TextView = itemView.findViewById(R.id.tvTinggiBadanUser)
        private val BeratBadanUser: TextView = itemView.findViewById(R.id.tvBeratBadanUser)
        private val StatusUser: TextView = itemView.findViewById(R.id.tvStatusUser)

        private val firestore = FirebaseFirestore.getInstance()
        private val userId = FirebaseAuth.getInstance().currentUser?.uid


        private fun saveVolumeToFirestore(volume: Int) {
            userId?.let { uid ->
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val data = mapOf("volumeMl" to volume)

                firestore.collection("users")
                    .document(uid)
                    .collection("asupanCairan")
                    .document(today)
                    .set(data)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Volume harian berhasil disimpan")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Gagal simpan volume", e)
                    }
            }
        }

        private fun getVolumeFromFirestore(onResult: (Int) -> Unit) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (userId == null) {
                onResult(0)
                return
            }

            firestore.collection("users")
                .document(userId)
                .collection("asupanCairan")
                .document(today)
                .get()
                .addOnSuccessListener { document ->
                    val volume = document.getLong("volumeMl")?.toInt() ?: 0
                    onResult(volume)
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Gagal ambil volume", it)
                    onResult(0)
                }
        }

        fun bind(volumeMl: Int) {
            getVolumeFromFirestore { volume ->
                currentVolumeMl = volume
                updateVolume(currentVolumeMl)
                loadData()

                btnMinus.setOnClickListener {
                    if (currentVolumeMl > 0) {
                        currentVolumeMl -= 250
                        updateVolume(currentVolumeMl)
                        saveVolumeToFirestore(currentVolumeMl)
                    }
                }

                btnPlus.setOnClickListener {
                    if (currentVolumeMl < 3000) {
                        currentVolumeMl += 250
                        updateVolume(currentVolumeMl)
                        saveVolumeToFirestore(currentVolumeMl)
                    }
                }

                llRekomendasiAktivitas.setOnClickListener {
                    val saran = tvSaranAktivitas.text.toString()
                    aktivitasClickListener.onRekomendasiAktivitasClicked(saran)
                }
            }
        }

        private fun updateVolume(volumeMl: Int) {
            val literText = String.format("%.2fL", volumeMl / 1000.0)
            tvVolume.text = literText
            progressBar.progress = volumeMl

            val colorRes = when (volumeMl) {
                in 0..600 -> R.color.progress_coklat
                in 601..1200 -> R.color.progress_kuning_gelap
                in 1201..1800 -> R.color.progress_kuning
                in 1801..2400 -> R.color.progress_kuning_putih
                in 2401..3000 -> R.color.progress_putih
                else -> R.color.progress_putih
            }

            progressBar.progressTintList =
                android.content.res.ColorStateList.valueOf(itemView.context.getColor(colorRes))
        }

        private fun loadData() {
            if (userId == null) {
                tvSaranAktivitas.text = "Data user tidak tersedia"
                return
            }

            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val kategoriIMT = document.getString("kategoriIMT") ?: "Kategori tidak tersedia"
                        val berat = document.getString("berat") ?: "Berat tidak tersedia"
                        val tinggi = document.getString("tinggi") ?: "Tinggi tidak tersedia"

                        tvSaranAktivitas.text = "Berdasarkan hasil pemeriksaan, Anda masuk dalam kategori $kategoriIMT. Berikut kami berikan beberapa pilihan olahraga yang cocok untuk membantu Anda."
                        TinggiBadanUser.text = "$berat cm"
                        BeratBadanUser.text = "$tinggi kg"

                        val statusUser = when (kategoriIMT) {
                            "Underweight" -> "Kekurangan berat badan"
                            "Normal" -> "Berat badan normal"
                            "Overweight" -> "Kelebihan berat badan"
                            else -> "Obesitas"
                        }
                        StatusUser.text = statusUser
                    } else {
                        tvSaranAktivitas.text = "Data kategori IMT tidak ditemukan"
                        TinggiBadanUser.text = "Data Tinggi tidak ditemukan"
                        BeratBadanUser.text = "Data Berat tidak ditemukan"
                        StatusUser.text = "Data tidak ditemukan"
                    }
                }
                .addOnFailureListener {
                    tvSaranAktivitas.text = "Gagal mengambil data kategori IMT"
                    TinggiBadanUser.text = "Gagal mengambil data Tinggi tidak ditemukan"
                    BeratBadanUser.text = "Gagal mengambil data Berat tidak ditemukan"
                    StatusUser.text = "Gagal mengambil data kategori IMT"
                }
        }
    }
}