package com.example.dishdash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishdash.Meal
import com.example.dishdash.MealAdapter
import com.example.dishdash.MealViewModel
import com.example.dishdash.R
import android.text.Editable
import android.text.TextWatcher


class SearchFragment : Fragment() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: MealAdapter
    private var mealList = mutableListOf<Meal>()

    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchInput = view.findViewById(R.id.searchInput)
        recyclerView = view.findViewById(R.id.recyclerSearchResults)
        progressBar = view.findViewById(R.id.progressBar)

        adapter = MealAdapter(
            meals = mealList,
            onItemClick = { meal ->
                Toast.makeText(requireContext(), "Clicked: ${meal.strMeal}", Toast.LENGTH_SHORT)
                    .show()
            },
            onFavoriteClick = { meal ->
                val isFav =
                    mealViewModel.favoriteMeals.value?.any { it.idMeal == meal.idMeal } == true
                if (isFav) {
                    mealViewModel.removeFromFavorites(meal.idMeal)
                    Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    mealViewModel.addToFavorites(meal)
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            isFavorite = { meal ->
                mealViewModel.favoriteMeals.value?.any { it.idMeal == meal.idMeal } == true
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        mealViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            mealList.clear()
            mealList.addAll(results)
            adapter.updateData(mealList)
            progressBar.visibility = View.GONE
        }

        mealViewModel.favoriteMeals.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        // ✨ Listen to text changes and search automatically
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    mealViewModel.searchMeals(query)
                } else {
                    mealList.clear()
                    adapter.updateData(mealList)
                }
            }
        })
    }
}