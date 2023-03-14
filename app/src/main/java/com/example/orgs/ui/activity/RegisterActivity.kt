package com.example.orgs.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.data.room.entity.UserEntity
import com.example.orgs.databinding.ActivityRegisterBinding
import com.example.orgs.util.openActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val userDao by lazy {
        AppDataBase.getInstance(this).userDao()
    }
    private val binding:ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_register_user)

        configureButtonSave()
    }

    private fun configureButtonSave() {
        binding.btnSave.setOnClickListener {
            val edtName = binding.edtName.text.toString()
            val edtUsername = binding.edtUsername.text.toString()
            val edtPassword = binding.edtPassword.text.toString()

            saveUser(UserEntity(name = edtName, username = edtUsername, password = edtPassword))
        }
    }

    //Como tratar erro em coroutinas
    private fun saveUser(user: UserEntity) {
        lifecycleScope.launch {
            try {
                userDao.saveUser(user)
                openActivity(ProductListActivity::class.java)
                finish()
            }catch (e:Exception){
                Toast.makeText(
                    this@RegisterActivity,
                    "Register error, Exeption: $e",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}