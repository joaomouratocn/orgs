package com.example.orgs.data.room.dao

import androidx.room.*
import com.example.orgs.data.room.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProductsWhitOutOrder():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    fun getAllProductsNameAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_name DESC")
    fun getAllProductsNameDesc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_desc ASC")
    fun getAllProductsDescAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_desc DESC")
    fun getAllProductsDescDesc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_price ASC")
    fun getAllProductsPriceAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_price DESC")
    fun getAllProductsPriceDesc():List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProduct(id : Int) : ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(vararg productEntity: ProductEntity)

    @Delete
    fun deleteProduct(productEntity: ProductEntity)
}