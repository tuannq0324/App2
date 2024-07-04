package com.example.app2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app2.database.model.ImageEntity
import com.example.app2.utils.Constants


@Database(
    entities = [ImageEntity::class], version = 7, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao


    companion object {
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java, Constants.DB_NAME
            ).fallbackToDestructiveMigration().build()
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) synchronized(this) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }
    }
}