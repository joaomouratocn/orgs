package com.example.orgs.util

import android.widget.ImageView
import coil.load
import com.example.orgs.R

fun ImageView.loadImage(image: String?) {
    load(image){
        fallback(R.drawable.imagem_padrao)
        error(R.drawable.erro)
        placeholder(R.drawable.loading)
    }
}