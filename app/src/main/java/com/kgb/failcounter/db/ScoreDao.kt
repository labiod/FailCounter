package com.kgb.failcounter.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.util.*

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores")
    fun getScores(): List<ScoreCounterEntity>

    @Query("SELECT * FROM scores WHERE date BETWEEN :from AND :to LIMIT 1")
    fun getScoreByDate(from: Date, to: Date): ScoreCounterEntity

    @Query("SELECT * FROM scores WHERE date BETWEEN :from AND :to")
    fun getScoresBetweenDates(from: Date, to: Date): List<ScoreCounterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: ScoreCounterEntity)

    @Query("DELETE FROM scores WHERE id = :itemId")
    fun deleteById(itemId: Int)

    @Query("DELETE FROM scores")
    fun clearAll()
}