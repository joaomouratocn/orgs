package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.orgs.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {
    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureButtonExit()
        lifecycleScope.launch {
            user.collect {
                loadsFields()
            }
        }
    }

    private fun configureButtonExit() {
        binding.btnExitApp.setOnClickListener {
            lifecycleScope.launch {
                singOut()
                finish()
            }
        }
    }

    private fun loadsFields() {
        user.value?.let {
            binding.txvName.text = it.name
            binding.txvUsername.text = it.username
        }
    }
}