package com.example.covary

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.work.* // Tambahkan import WorkManager

import com.example.covary.worker.SaveVolumeWorker // Tambahkan juga ini
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.covary.databinding.ActivityMainBinding
import com.example.covary.fragments.DaftarMakananFragment
import com.example.covary.fragments.DailyFragment
import com.example.covary.fragments.HistoryFragment
import com.example.covary.fragments.HomeFragment
import com.example.covary.fragments.ProfileFragment
import com.google.firebase.FirebaseApp
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1, "Home", R.drawable.ic_home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2, "Daily", R.drawable.ic_daily)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3, "History", R.drawable.ic_history)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(4, "Profile", R.drawable.ic_profile)
        )

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                    replaceFragment(HomeFragment())
                }
                2 -> {
                    replaceFragment(DailyFragment())
                }
                3 -> {
                    replaceFragment(HistoryFragment())
                }
                4 -> {
                    replaceFragment(ProfileFragment())
                }
            }
        }

        setupDailySaveWorker()
        replaceFragment(HomeFragment())
        bottomNavigation.show(1)
    }

    private fun setupDailySaveWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<SaveVolumeWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelayToMidnight(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SaveVolumeDaily",
            ExistingPeriodicWorkPolicy.KEEP, // ✅ supaya tidak double saat buka ulang app
            dailyWorkRequest
        )

        Log.d("MainActivity", "SaveVolumeWorker scheduled")
    }

    private fun calculateInitialDelayToMidnight(): Long {
        val now = Calendar.getInstance()
        val nextMidnight = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1) // besok
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return nextMidnight.timeInMillis - now.timeInMillis
    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("MainActivity", "Replacing fragment with ${fragment.javaClass.simpleName}")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateToDaftarMakanan() {
        Log.d("MainActivity", "navigateToDaftarMakanan called")
        replaceFragment(DaftarMakananFragment())
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            // Jika sudah di root fragment, keluar aplikasi
            finish()
        }
    }
}