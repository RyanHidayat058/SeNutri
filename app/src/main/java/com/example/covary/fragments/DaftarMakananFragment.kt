package com.example.covary.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.covary.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covary.retrofit.MenuAdapter
import com.example.covary.retrofit.MenuItem
import com.example.covary.retrofit.RetrofitClient
import kotlinx.coroutines.*

class DaftarMakananFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var menuAdapter: MenuAdapter

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_makanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvDaftarMakanan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchBar = view.findViewById(R.id.searchBar)
        setupSearchBar()

        val backButton: ImageButton? = view.findViewById(R.id.btnBack)
        backButton?.setOnClickListener {
            val fm = parentFragmentManager
            if (fm.backStackEntryCount > 0) {
                fm.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

        if (!::menuAdapter.isInitialized) {
            fetchData()
        } else {
            recyclerView.adapter = menuAdapter
        }
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                menuAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchData() {
        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.getMenu()
                }

                Log.d("DaftarMakananFragment", "Response received: ${response.Menu.size} items")

                menuAdapter = MenuAdapter(response.Menu, object : MenuAdapter.OnItemClickListener {
                    override fun onAddClicked(item: MenuItem) {
                        val jenisMakanan = arguments?.getString("jenisMakanan")

                        val bundle = Bundle().apply {
                            putString("jenisMakanan", jenisMakanan)
                            putParcelable("menuItem", item)
                        }

                        val fragment = DetailDaftarMakananFragment().apply {
                            arguments = bundle
                        }

                        parentFragmentManager.beginTransaction()
                            .hide(this@DaftarMakananFragment)
                            .add(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                })

                recyclerView.adapter = menuAdapter

            } catch (e: Exception) {
                Log.e("DaftarMakananFragment", "Error fetching data", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }
}