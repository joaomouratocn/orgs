package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.data.room.entity.UserEntity
import com.example.orgs.util.LOGGED_USER
import com.example.orgs.util.dataStore
import com.example.orgs.util.openActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class BaseActivity : AppCompatActivity() {
    protected val userDao by lazy {
        AppDataBase.getInstance(this).userDao()
    }

    private val _user: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
    protected val user: StateFlow<UserEntity?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            verifyLoggedUser()
        }
    }

    private suspend fun verifyLoggedUser() {
        dataStore.data.collect { preferences ->
            preferences[LOGGED_USER]?.let { userId ->
                getUser(userId)
            } ?: goToLogin()
        }
    }

    private fun goToLogin() {
        openActivity(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    private suspend fun getUser(userId: Int): UserEntity? {
        return userDao
            .getUserById(userId)
            .firstOrNull().also {
                _user.value = it
            }
    }

    protected suspend fun singOut() {
        dataStore.edit { preferences ->
            preferences.remove(LOGGED_USER)
        }
    }
}