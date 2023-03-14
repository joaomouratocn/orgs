package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.util.*
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val userDao by lazy {
        AppDataBase.getInstance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configureBtnLogin()
        configureClickTxvNewAccount()
    }

    private fun configureBtnLogin() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            tryMakeLogin(username, password)
        }
    }

    private fun tryMakeLogin(username: String, password: String) {
        lifecycleScope.launch {
            userDao.getUser(username, password)?.let {
                dataStore.edit { preferences ->
                    preferences[LOGGED_USER] = it.id
                    openActivity(ProductListActivity::class.java){
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    finish()
                }
            } ?: toast(R.string.str_error_on_authentication)
        }
    }

    private fun configureClickTxvNewAccount() {
        binding.txvRegister.setOnClickListener {
            openActivity(RegisterActivity::class.java){
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
    }

}