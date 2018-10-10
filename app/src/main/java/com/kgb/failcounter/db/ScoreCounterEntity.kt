package com.kgb.failcounter.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "scores")
class ScoreCounterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val score: Int = 0,
    val date: Date
) {
    override fun toString(): String {
        return "ScoreCounterEntity(id=$id, score=$score, date=$date)"
    }
}