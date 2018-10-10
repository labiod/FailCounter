package com.kgb.failcounter

import com.kgb.failcounter.viewmodel.ScoreCounterViewModel

class CounterUtil {
    companion object {
        fun getScore(model: ScoreCounterViewModel, defValue: Int): Int {
            return defValue
        }
    }
}