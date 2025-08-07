package com.example.dishdash.fragments

import FavoriteMealsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishdash.local.AppDatabase
import com.example.dishdash.R
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var recyclerFavorites: RecyclerView
    private lateinit var adapter: FavoriteMealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerFavorites = view.findViewById(R.id.recyclerFavorites)
        recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())

        adapter = FavoriteMealsAdapter(mutableListOf()) { meal ->
            lifecycleScope.launch {
                val dao = AppDatabase.getDatabase(requireContext()).favoriteMealDao()
                dao.delete(meal)
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                loadMeals()
            }
        }

        recyclerFavorites.adapter = adapter
        loadMeals()
    }

    private fun loadMeals() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).favoriteMealDao()
            val updatedMeals = dao.getAll()
            adapter.updateData(updatedMeals)
        }
    }
}
