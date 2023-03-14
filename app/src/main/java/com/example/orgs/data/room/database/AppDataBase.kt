package com.example.orgs.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.orgs.data.room.converter.Converters
import com.example.orgs.data.room.dao.ProductDao
import com.example.orgs.data.room.dao.UserDao
import com.example.orgs.data.room.entity.ProductEntity
import com.example.orgs.data.room.entity.UserEntity
import com.example.orgs.data.room.migrations.MIGRATIONS_1_2
import com.example.orgs.data.room.migrations.MIGRATION_2_3

@Database(
    entities = [ProductEntity::class, UserEntity::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var db: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            return db ?: Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "orgs.db"
            ).addMigrations(
                MIGRATIONS_1_2,
                MIGRATION_2_3
            )
                //.fallbackToDestructiveMigration() RECURSO QUE APAGA TODOS OS DADOS DO BANCO DE DADOS,
                // USADO APENAS NO DESENVOLVIMENTO DO APLICAIVO
                .build().also {
                    db = it
                }
        }
    }
}