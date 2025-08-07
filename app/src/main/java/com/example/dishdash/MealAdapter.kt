package com.example.dishdash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealAdapter(
    private var meals: List<Meal>,
    private val onItemClick: (Meal) -> Unit,
    private val onFavoriteClick: (Meal) -> Unit,
    private val isFavorite: (Meal) -> Boolean
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageMeal: ImageView = itemView.findViewById(R.id.imageMeal)
        val textMealName: TextView = itemView.findViewById(R.id.textMealName)
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_card, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        val context = holder.itemView.context

        holder.textMealName.text = meal.strMeal
        holder.textCategory.text = meal.strCategory
        Glide.with(context).load(meal.strMealThumb).into(holder.imageMeal)

        updateFavoriteIcon(holder.btnFavorite, isFavorite(meal))

        holder.itemView.setOnClickListener { onItemClick(meal) }

        holder.btnFavorite.setOnClickListener { onFavoriteClick(meal) }
    }

    private fun updateFavoriteIcon(button: ImageButton, isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
        button.setImageResource(iconRes)
    }

    override fun getItemCount() = meals.size

    fun updateData(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}
