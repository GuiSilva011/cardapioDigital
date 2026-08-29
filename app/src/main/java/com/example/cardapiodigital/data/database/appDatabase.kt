package com.example.cardapiodigital.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardapiodigital.data.dao.BebidaDao
import com.example.cardapiodigital.data.dao.CardapioBebidaDao
import com.example.cardapiodigital.data.dao.CardapioDao
import com.example.cardapiodigital.data.entity.BebidaEntity
import com.example.cardapiodigital.data.entity.CardapioBebidaEntity
import com.example.cardapiodigital.data.entity.CardapioEntity

@Database(
    entities = [
        BebidaEntity::class,
        CardapioEntity::class,
        CardapioBebidaEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bebidaDao(): BebidaDao

    abstract fun cardapioDao(): CardapioDao

    abstract fun cardapioBebidaDao(): CardapioBebidaDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cardapio_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}