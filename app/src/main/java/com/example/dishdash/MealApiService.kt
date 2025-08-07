package com.example.dishdash


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealResponse>

    @GET("search.php")
    fun searchMeal(@Query("s") name: String): Call<MealResponse>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") id: String): Call<MealResponse>


    @GET("search.php")
    fun getMealsByFirstLetter(@Query("f") letter: String): Call<MealResponse>

    @GET("random.php")
    fun getRandomMeal(): Call<MealResponse>

}
