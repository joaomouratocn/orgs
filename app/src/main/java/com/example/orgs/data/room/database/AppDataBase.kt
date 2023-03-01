package com.example.orgs.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.orgs.data.room.converter.Converters
import com.example.orgs.data.room.dao.ProductDao
import com.example.orgs.data.room.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object{
        @Volatile private var db : AppDataBase? = null
        fun getInstance(context: Context):AppDataBase{
            return db ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "orgs.db"
            ).build().also {
                db = it
            }
        }
    }
}