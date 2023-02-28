package com.example.orgs.util

import android.widget.ImageView
import coil.load
import com.example.orgs.R
import com.example.orgs.data.room.entity.ProductEntity
import com.example.orgs.model.Product

fun ImageView.loadImage(image: String?) {
    load(image){
        fallback(R.drawable.imagem_padrao)
        error(R.drawable.erro)
        placeholder(R.drawable.loading)
    }
}

fun Product.toProductEntity():ProductEntity{
    return ProductEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        image = this.image
    )
}

fun ProductEntity.toProduct():Product{
    return Product(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        image = this.image
    )
}

fun List<ProductEntity>.toListProduct():List<Product>{
    val listProduct = mutableListOf<Product>()
    this.forEach{productEntity ->
        productEntity.toProduct()
        listProduct.add(productEntity.toProduct())
    }
    return listProduct
}