package com.example.fitpho.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(GuideEntity::class, GuideDetailEntity::class), version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun guideDao(): GuideDao

    companion object {
        private var database: AppDatabase? = null
        private const val ROOM_DB = "Fitpho.db"

        fun getDatabase(context: Context): AppDatabase {
            if(database == null){
                database = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "SystemItem.db"
                )
                    //.fallbackToDestructiveMigration()
                    .createFromAsset("database/SystemItem.db")
                    .build()
            }
            return database!!
        }
    }




}