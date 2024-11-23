package com.example.lynq.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lynq.data.local.entity.StoriesEntity

@Database(entities = [StoriesEntity::class], version = 1, exportSchema = false)
abstract class LynqDatabase: RoomDatabase() {
    abstract fun storiesDao(): StoriesDao
    companion object {
        @Volatile
        private var instance: LynqDatabase? = null
        fun getInstance(context: Context): LynqDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder( context.applicationContext,
                    LynqDatabase::class.java, "stories.db"
                ).allowMainThreadQueries().build()
            }
    }
}