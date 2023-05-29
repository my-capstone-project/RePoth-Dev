package com.capstone.repoth.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.repoth.data.model.ListRepoth

@Database(
    entities = [ListRepoth::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class RepothDatabase : RoomDatabase() {
    abstract fun repothDao(): RepothDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    companion object {
        @Volatile
        private var INSTANCE: RepothDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): RepothDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RepothDatabase::class.java, "repoth_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}