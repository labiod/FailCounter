package com.kgb.failcounter.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.kgb.kapp.db.converter.DateConverter

@Database(entities = [ScoreCounterEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ScoreCounterDB : RoomDatabase() {
    abstract fun noteDao() : ScoreDao

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        @Volatile
        private var instance : ScoreCounterDB? = null

        val lock = Any()

        @JvmStatic
        fun getInstance(context: Context) : ScoreCounterDB {
            if (instance == null) {
                synchronized(lock) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ScoreCounterDB::class.java,
                            ScoreCounterDB.DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}