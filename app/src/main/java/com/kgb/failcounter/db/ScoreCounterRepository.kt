package com.kgb.failcounter.db

import android.content.Context
import java.util.*
import java.util.concurrent.Executors

class ScoreCounterRepository private constructor(context: Context) {
    val db = ScoreCounterDB.getInstance(context)
    val executor = Executors.newSingleThreadExecutor()

    fun getScore(date: Date): ScoreCounterEntity {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val from = calendar.time
        calendar.set(Calendar.HOUR, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 99)
        val to = calendar.time
        return db.noteDao().getScoreByDate(from , to)
    }

    fun update(item: ScoreCounterEntity) {
        executor.execute {
            db.noteDao().update(item)
        }
    }

    fun deleteScore(item: ScoreCounterEntity) {
        executor.execute {
            item.id?.let {
                db.noteDao().deleteById(it)
            }
        }
    }

    companion object {
        @Volatile
        var instance: ScoreCounterRepository? = null
        val lock = Any()

        fun getInstance(context: Context): ScoreCounterRepository {
            if (instance == null) {
                synchronized(lock) {
                    if (instance == null) {
                        instance = ScoreCounterRepository(context)
                    }
                }
            }
            return instance!!
        }
    }
}