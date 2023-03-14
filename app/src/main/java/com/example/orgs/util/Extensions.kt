package com.example.orgs.util

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import coil.load
import com.example.orgs.R

fun ImageView.loadImage(image: String?) {
    load(image) {
        fallback(R.drawable.imagem_padrao)
        error(R.drawable.erro)
        placeholder(R.drawable.loading)
    }
}

fun Context.openActivity(
    clazz: Class<*>,
    intent: Intent.() -> Unit = {}
) {
    Intent(this, clazz)
        .apply {
            intent()
            startActivity(this)
        }
}

fun Context.toast(message: Int){
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}