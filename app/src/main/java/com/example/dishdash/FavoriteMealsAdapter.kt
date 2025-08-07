
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dishdash.R
import com.example.dishdash.local.FavoriteMeal


class FavoriteMealsAdapter(
    private var meals: List<FavoriteMeal>,
    private val onRemoveClick: (FavoriteMeal) -> Unit
) : RecyclerView.Adapter<FavoriteMealsAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageMeal: ImageView = itemView.findViewById(R.id.imageMeal)
        val textMealName: TextView = itemView.findViewById(R.id.textMealName)
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_card, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val meal = meals[position]
        holder.textMealName.text = meal.strMeal
        holder.textCategory.text = meal.strCategory
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.imageMeal)

        holder.btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        holder.btnFavorite.setOnClickListener {
            onRemoveClick(meal)
        }
    }

    override fun getItemCount() = meals.size

    fun updateData(newMeals: List<FavoriteMeal>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}
