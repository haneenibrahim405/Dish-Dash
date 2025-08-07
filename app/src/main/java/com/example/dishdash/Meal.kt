package com.example.dishdash

import com.example.dishdash.local.FavoriteMeal
import java.io.Serializable

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strCategory: String,
    val strInstructions: String
)  {

    fun toFavorite(): FavoriteMeal {
        return FavoriteMeal(
            idMeal = idMeal,
            strMeal = strMeal,
            strMealThumb = strMealThumb,
            strCategory = strCategory,
            strInstructions = strInstructions
        )
    }
}
