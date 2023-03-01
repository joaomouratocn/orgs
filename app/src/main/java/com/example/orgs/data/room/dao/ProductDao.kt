package com.example.orgs.data.room.dao

import androidx.room.*
import com.example.orgs.data.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProductsWhitOutOrder():Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY product_name ASC")
    suspend fun getAllProductsNameAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_name DESC")
    suspend fun getAllProductsNameDesc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_desc ASC")
    suspend fun getAllProductsDescAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_desc DESC")
    suspend fun getAllProductsDescDesc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_price ASC")
    suspend fun getAllProductsPriceAsc():List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY product_price DESC")
    suspend fun getAllProductsPriceDesc():List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProduct(id : Int) : Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductId(id : Int) : ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)
}