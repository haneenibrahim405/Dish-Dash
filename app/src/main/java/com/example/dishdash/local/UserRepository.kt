package com.example.dishdash.local

import android.content.Context
import com.example.dishdash.local.AppDatabase
import com.example.dishdash.local.UserEntity

class UserRepository(context: Context) {

    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun registerUser(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.getUser(email)
        return user != null && user.password == password
    }
}
