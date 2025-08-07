package com.example.dishdash.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dishdash.local.AppDatabase
import com.example.dishdash.R
import com.example.dishdash.local.FavoriteMeal
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private lateinit var imageMeal: ImageView
    private lateinit var textMealName: TextView
    private lateinit var textCategory: TextView
    private lateinit var textInstructions: TextView
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnYoutube: ImageButton

    private var idMeal: String? = null
    private var strMeal: String? = null
    private var strMealThumb: String? = null
    private var strCategory: String? = null
    private var strInstructions: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageMeal = view.findViewById(R.id.imageMeal)
        textMealName = view.findViewById(R.id.textMealName)
        textCategory = view.findViewById(R.id.textCategory)
        textInstructions = view.findViewById(R.id.textInstructions)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        btnYoutube = view.findViewById(R.id.btnYoutube)


        arguments?.let { bundle ->
            idMeal = bundle.getString("idMeal")
            strMeal = bundle.getString("strMeal")
            strMealThumb = bundle.getString("strMealThumb")
            strCategory = bundle.getString("strCategory")
            strInstructions = bundle.getString("strInstructions")
        }

        if (idMeal.isNullOrEmpty() || strMeal.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Meal data not found!", Toast.LENGTH_SHORT).show()
            return
        }

        bindMealData()
    }

    private fun bindMealData() {
        textMealName.text = strMeal
        textCategory.text = strCategory ?: "No Category"
        textInstructions.text = strInstructions ?: "No Instructions"

        Glide.with(requireContext())
            .load(strMealThumb)
            .error(R.drawable.ic_image_error)
            .into(imageMeal)

        val dao = AppDatabase.getDatabase(requireContext()).favoriteMealDao()

        lifecycleScope.launch {
            val isFav = dao.getAll().any { it.idMeal == idMeal }
            updateFavoriteIcon(isFav)
        }

        btnFavorite.setOnClickListener {
            lifecycleScope.launch {
                val nowFav = dao.getAll().any { it.idMeal == idMeal }
                if (nowFav) {
                    dao.delete(
                        FavoriteMeal(
                            idMeal ?: "",
                            strMeal ?: "",
                            strMealThumb ?: "",
                            strCategory ?: "",
                            strInstructions ?: ""
                        )
                    )
                    updateFavoriteIcon(false)
                    Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    dao.insert(
                        FavoriteMeal(
                            idMeal ?: "",
                            strMeal ?: "",
                            strMealThumb ?: "",
                            strCategory ?: "",
                            strInstructions ?: ""
                        )
                    )
                    updateFavoriteIcon(true)
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnYoutube.setOnClickListener {
            val youtubeUrl = "https://www.youtube.com/results?search_query=$strMeal"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            startActivity(intent)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val icon = if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
        btnFavorite.setImageResource(icon)
    }
}
