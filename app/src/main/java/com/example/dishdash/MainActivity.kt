package com.example.dishdash

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dishdash.activity.AuthActivity
import com.example.dishdash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var backPressTime = 0L
    private val BACK_PRESS_INTERVAL = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(this)
        if (!sharedPref.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.btmNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.btmNav.visibility = when (destination.id) {
                R.id.splashFragment, R.id.authActivity -> android.view.View.GONE
                else -> android.view.View.VISIBLE
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressTime < BACK_PRESS_INTERVAL) {
                        finish()
                    } else {
                        backPressTime = currentTime
                        android.widget.Toast.makeText(
                            this@MainActivity,
                            "Press back again to exit",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}
