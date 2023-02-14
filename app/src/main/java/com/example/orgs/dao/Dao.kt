package com.example.orgs.dao

import com.example.orgs.model.Product

class Dao {
    companion object{
        private var contId = 1
        private val productList = mutableListOf<Product>()
    }

    fun getAllProducts():List<Product>{
        return  productList.toList()
    }

    fun addProduct(product: Product){
        product.id = contId
        productList.add(product)
        incrementId()
    }

    fun updateProduct(product: Product) {
        val index = getIndex(product)
        productList[index] = product
    }

    fun removeProduct(product: Product){
        productList.remove(product)
    }

    private fun getIndex(product: Product) : Int{
        productList.forEach{
            if (it.id == product.id) {
                return productList.indexOf(it)
            }
        }
        return 0
    }

    private fun incrementId() {
        contId++
    }
}