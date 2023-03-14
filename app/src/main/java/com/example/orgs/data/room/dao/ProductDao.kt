package com.example.orgs.data.room.dao

import androidx.room.*
import com.example.orgs.data.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE user_id = :userId")
    fun getAllProductsWhitOutOrder(userId : Int):Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE user_id = :userId ORDER BY product_name ASC")
    fun getProductsNameAsc(userId: Int):Flow<List<ProductEntity>?>

    @Query("SELECT * FROM products WHERE user_id = :userId ORDER BY product_name DESC")
    fun getProductsNameDesc(userId: Int):Flow<List<ProductEntity>?>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProduct(id : Int) : Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductId(id : Int) : ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(vararg productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)
}