package com.example.orgs.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val username:String,
    val password:String
)