package com.example.dishdash.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dishdash.R
import com.example.dishdash.fragments.FavoriteFragment
import com.example.dishdash.fragments.HomeFragment
import com.example.dishdash.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.dishdash.SharedPref
import android.content.Intent
import androidx.activity.OnBackPressedCallback

class RecipeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var backPressedTime: Long = 0
    private val BACK_PRESS_INTERVAL = 2000L

    private var currentFragment: Fragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = SharedPref(this)
        if (!prefs.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_recipe)

        bottomNav = findViewById(R.id.bottomNav)
        loadFragment(currentFragment)

        bottomNav.setOnItemSelectedListener {
            currentFragment = when (it.itemId) {
                R.id.homeFragment -> HomeFragment()
                R.id.favoritesFragment -> FavoriteFragment()
                R.id.searchFragment -> SearchFragment()
                else -> HomeFragment()
            }
            loadFragment(currentFragment)
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentFragment !is HomeFragment) {
                    currentFragment = HomeFragment()
                    bottomNav.selectedItemId = R.id.homeFragment
                    loadFragment(currentFragment)
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < BACK_PRESS_INTERVAL) {
                        finish()
                    } else {
                        backPressedTime = currentTime
                        Toast.makeText(
                            this@RecipeActivity,
                            "Press back again to exit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
