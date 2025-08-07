package com.example.dishdash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishdash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MealAdapter
    private val mealList = mutableListOf<Meal>()

    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerHome)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MealAdapter(
            meals = mealList,
            onItemClick = { meal ->
                val bundle = Bundle().apply {
                    putString("idMeal", meal.idMeal)
                    putString("strMeal", meal.strMeal)
                    putString("strMealThumb", meal.strMealThumb)
                    putString("strCategory", meal.strCategory)
                    putString("strInstructions", meal.strInstructions)
                }
                findNavController().navigate(R.id.action_homeFragment_to_recipeDetailFragment, bundle)
            },

            onFavoriteClick = { meal ->
                val isFav = mealViewModel.favoriteMeals.value?.any { it.idMeal == meal.idMeal } == true
                if (isFav) {
                    mealViewModel.removeFromFavorites(meal.idMeal)
                    Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    mealViewModel.addToFavorites(meal)
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            },
            isFavorite = { meal ->
                mealViewModel.favoriteMeals.value?.any { it.idMeal == meal.idMeal } == true
            }
        )

        recyclerView.adapter = adapter

        mealViewModel.favoriteMeals.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        loadMealsByFirstLetter("c")

        return view
    }

    private fun loadMealsByFirstLetter(letter: String) {
        ApiClient.apiService.getMealsByFirstLetter(letter)
            .enqueue(object : Callback<MealResponse> {
                override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                    response.body()?.meals?.let {
                        mealList.clear()
                        mealList.addAll(it)
                        adapter.updateData(mealList)
                    } ?: Toast.makeText(requireContext(), "No meals found", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
