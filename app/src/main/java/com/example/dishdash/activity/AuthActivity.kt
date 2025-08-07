package com.example.dishdash.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.dishdash.fragments.LoginFragment
import com.example.dishdash.fragments.RegisterFragment
import com.example.dishdash.local.AppDatabase
import com.example.dishdash.R
import com.example.dishdash.local.UserEntity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.dishdash.SharedPref



class AuthActivity : AppCompatActivity(),
    LoginFragment.LoginListener,
    RegisterFragment.RegisterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            val fragment = if (intent.getBooleanExtra("show_login", true)) {
                LoginFragment()
            } else {
                RegisterFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host, fragment)
                .commit()
        }
    }
    override fun onLogin(email: String, password: String) {
        lifecycleScope.launch {
            val userDao = AppDatabase.getDatabase(this@AuthActivity).userDao()
            val user = userDao.getUser(email)

            if (user != null && user.password == password) {
                val sharedPref = SharedPref(this@AuthActivity)
                sharedPref.setLogin(email)

                startActivity(Intent(this@AuthActivity, RecipeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@AuthActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRegister(name: String, email: String, password: String) {
        lifecycleScope.launch {
            val userDao = AppDatabase.getDatabase(this@AuthActivity).userDao()
            val existingUser = userDao.getUser(email)

            if (existingUser != null) {
                Toast.makeText(this@AuthActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val newUser = UserEntity(name = name, email = email, password = password)
            userDao.insert(newUser)

            Toast.makeText(this@AuthActivity, "Registration successful", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }


    fun navigateToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

    fun navigateToRegister() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }
}


