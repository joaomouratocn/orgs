package com.example.orgs.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.orgs.data.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun saveUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username:String, password:String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId:Int) : Flow<UserEntity>

    @Query("SELECT * FROM users")
    fun getAllUsers() : Flow<List<UserEntity>?>
}