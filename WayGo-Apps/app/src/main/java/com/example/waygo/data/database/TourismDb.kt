package com.example.waygo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.waygo.data.response.TourismItems

@Database(
    entities = [TourismItems::class],
    version = 1,
    exportSchema = false
)
abstract class TourismDb : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: TourismDb? = null

        @JvmStatic
        fun getDatabase(context: Context): TourismDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TourismDb::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}