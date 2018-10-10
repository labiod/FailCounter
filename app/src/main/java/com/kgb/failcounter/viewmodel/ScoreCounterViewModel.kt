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

class ScoreCounterViewModel(application: Application, val date: Date) : AndroidViewModel(application) {
    private val repository = ScoreCounterRepository.getInstance(application)
    private val _score = MutableLiveData<ScoreCounterEntity>()
    private val executor = Executors.newSingleThreadExecutor()
    val score : LiveData<ScoreCounterEntity?>
        get() = _score

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

    fun getAll(): LiveData<List<ScoreCounterEntity>> {
        return repository.db.noteDao().getScores()
    }

    fun clearAll() {
        executor.execute {
            repository.db.noteDao().clearAll()
        }
    }

    private fun loadData() {
        executor.execute {
            val data = repository.getScore(date)
            _score.postValue(data)
        }
    }
}