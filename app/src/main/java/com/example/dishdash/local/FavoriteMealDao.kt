package com.example.dishdash.local


import androidx.room.*


@Dao
interface FavoriteMealDao {
    @Query("SELECT * FROM favorite_meals")
    suspend fun getAll(): List<FavoriteMeal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: FavoriteMeal)

    @Delete
    suspend fun delete(meal: FavoriteMeal)

    @Query("DELETE FROM favorite_meals WHERE idMeal = :mealId")
    suspend fun deleteById(mealId: String)
}

