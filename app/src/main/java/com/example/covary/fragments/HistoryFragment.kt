package com.example.covary.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.R
import com.example.covary.adapterHistory.HistoryAdapter
import com.example.covary.adapterHistory.HistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var firestore: FirebaseFirestore
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        firestore = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(requireContext(), "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            Log.e("FirebaseAuth", "User belum login.")
            return
        }

        userId = user.uid
        Log.d("FirebaseAuth", "User ID: $userId")

        // 🔥 Ambil data dari /users/{uid}/historyIndex/index
        val indexRef = firestore.collection("users")
            .document(userId!!)
            .collection("historyIndex")
            .document("index")

        indexRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val tanggalList = document["tanggal"] as? List<*> ?: listOf<Any>()

                    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("in", "ID"))

                    val historyList = tanggalList.mapNotNull {
                        it?.toString()
                    }.mapNotNull { dateStr ->
                        try {
                            val date = dateFormat.parse(dateStr)
                            Pair(date, dateStr)
                        } catch (e: Exception) {
                            Log.e("ParseError", "Gagal parse tanggal: $dateStr", e)
                            null
                        }
                    }.sortedBy { it.first }
                        .map { (_, dateStr) ->
                            HistoryItem(dateStr)
                        }

                    if (historyList.isEmpty()) {
                        Toast.makeText(requireContext(), "Riwayat masih kosong", Toast.LENGTH_SHORT).show()
                    }

                    adapter = HistoryAdapter(historyList) { selectedDate ->
                        val detailFragment = DetailHistoryFragment()
                        detailFragment.arguments = Bundle().apply {
                            putString("tanggal", selectedDate)
                        }

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }

                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Riwayat belum tersedia", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Gagal mengambil riwayat", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Gagal ambil historyIndex", e)
            }
    }
}