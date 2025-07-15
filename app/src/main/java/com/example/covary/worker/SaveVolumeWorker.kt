package com.example.covary.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveVolumeWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val context = applicationContext
        val prefs = context.getSharedPreferences("volume_prefs", Context.MODE_PRIVATE)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return Result.retry()

        val firestore = FirebaseFirestore.getInstance()

        val volumeMl = prefs.getInt("current_volume", 0)

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val data = hashMapOf("volumeMl" to volumeMl)

        return try {
            Tasks.await(
                firestore.collection("users")
                    .document(userId)
                    .collection("asupanCairan")
                    .document(today)
                    .set(data)
            )

            prefs.edit().putInt("current_volume", 0).apply()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}