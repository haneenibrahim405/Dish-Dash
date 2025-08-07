package com.example.dishdash

import android.app.Application
import androidx.lifecycle.*
import com.example.dishdash.local.AppDatabase
import com.example.dishdash.local.FavoriteMeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {

    val searchResults = MutableLiveData<List<Meal>>()
    private val dao = AppDatabase.getDatabase(application).favoriteMealDao()

    private val _favoriteMeals = MutableLiveData<List<FavoriteMeal>>()
    val favoriteMeals: LiveData<List<FavoriteMeal>> get() = _favoriteMeals

    init {
        loadFavoriteMeals()
    }

    fun searchMeals(query: String) {
        ApiClient.apiService.searchMeal(query).enqueue(object : retrofit2.Callback<MealResponse> {
            override fun onResponse(call: retrofit2.Call<MealResponse>, response: retrofit2.Response<MealResponse>) {
                val meals = response.body()?.meals ?: emptyList()
                searchResults.postValue(meals)
            }

            override fun onFailure(call: retrofit2.Call<MealResponse>, t: Throwable) {
                searchResults.postValue(emptyList())
            }
        })
    }

    fun loadFavoriteMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            val meals = dao.getAll()
            _favoriteMeals.postValue(meals)
        }
    }

    fun addToFavorites(meal: Meal) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(
                FavoriteMeal(
                    idMeal = meal.idMeal,
                    strMeal = meal.strMeal,
                    strMealThumb = meal.strMealThumb,
                    strCategory = meal.strCategory,
                    strInstructions = meal.strInstructions
                )
            )
            loadFavoriteMeals()
        }
    }

    fun removeFromFavorites(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(mealId)
            loadFavoriteMeals()
        }
    }
}
