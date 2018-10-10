package com.kgb.failcounter.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.kgb.failcounter.App
import java.util.*

class ScoreCounterViewModelFactory(val date: Date) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            return ScoreCounterViewModel(App.instance, date) as T
        } catch (ex : InstantiationException) {
            return super.create(modelClass)
        }
    }
}