package com.kgb.failcounter.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.kgb.failcounter.db.ScoreCounterEntity
import com.kgb.failcounter.db.ScoreCounterRepository
import java.util.*
import java.util.concurrent.Executors
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class ScoreCounterViewModel(application: Application, val date: Date) : AndroidViewModel(application) {
    private val repository = ScoreCounterRepository.getInstance(application)
    private val _score = MutableLiveData<ScoreCounterEntity>()
    private val _lastScores = MutableLiveData<List<ScoreCounterEntity>>()
    private val executor = Executors.newSingleThreadExecutor()
    val score : LiveData<ScoreCounterEntity?>
        get() = _score

    val lastScores : LiveData<List<ScoreCounterEntity>>
        get() = _lastScores

    init {
        loadData()
    }

    fun updateScore(score: Int) {
        val newScore = _score.value?.let {
            ScoreCounterEntity(it.id, score, it.date)
        } ?: ScoreCounterEntity(null, score, date)
        repository.update(newScore)
    }

    fun resetCounter(item: ScoreCounterEntity) {
        repository.deleteScore(item)
    }

    fun clearIncorrectData() {
        executor.execute {
            val scores = repository.db.noteDao().getScores()
            val correctScore = ArrayList<ScoreCounterEntity>()
            val incorrectScores = ArrayList<ScoreCounterEntity>()
            for (score in scores) {
                if (containsScoreWithDate(correctScore, score.date))
                    repository.deleteScore(score)
                else
                    correctScore.add(score)
            }
        }
    }

    private fun loadData() {
        executor.execute {
            val data = repository.getScoresFromSevenDays(date)
            val prevScores = ArrayList<ScoreCounterEntity>()

            for (score in data) {
                if(checkDate(date, score.date)) {
                    _score.postValue(score)
                } else {
                    prevScores.add(score)
                }
            }
            _lastScores.postValue(prevScores)
        }
    }

    private fun containsScoreWithDate(scores: List<ScoreCounterEntity>, date: Date): Boolean {
        for (score in scores) {
            if (checkDate(score.date, date)) {
                return true
            }
        }
        return false
    }

    private fun checkDate(date1: Date, date2: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val month1 = calendar.get(Calendar.MONTH)
        val year1 = calendar.get(Calendar.YEAR)
        calendar.time = date2
        val day2 = calendar.get(Calendar.DAY_OF_MONTH)
        val month2 = calendar.get(Calendar.MONTH)
        val year2 = calendar.get(Calendar.YEAR)
        return day1 == day2 && month1 == month2 && year1 == year2
    }
}