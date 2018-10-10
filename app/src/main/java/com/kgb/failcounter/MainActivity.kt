package com.kgb.failcounter

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import com.kgb.failcounter.databinding.ActivityMainBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kgb.failcounter.db.ScoreCounterEntity
import com.kgb.failcounter.viewmodel.ScoreCounterViewModel
import com.kgb.failcounter.viewmodel.ScoreCounterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var model : ScoreCounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        model =  ViewModelProviders.of(this, ScoreCounterViewModelFactory(Date())).get(ScoreCounterViewModel::class.java)
        model.score.observe(this, android.arch.lifecycle.Observer {
            Log.d("[KGB]", "load data : $it")
            binding.counterRoundButton.counter = it?.score ?: 0
        })
    }

    override fun onStop() {
        model.updateScore(counterRoundButton.counter)
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId ?: 0
        return when(id) {
            R.id.main_clear_data -> {
                clearAll()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun clearAll() {
        model.clearAll()
    }
}
