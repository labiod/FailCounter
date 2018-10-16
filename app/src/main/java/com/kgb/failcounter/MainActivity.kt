package com.kgb.failcounter

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import com.kgb.failcounter.databinding.ActivityMainBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kgb.failcounter.adapter.PrevScoreAdapter
import com.kgb.failcounter.view.CounterRoundButton
import com.kgb.failcounter.viewmodel.ScoreCounterViewModel
import com.kgb.failcounter.viewmodel.ScoreCounterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var model : ScoreCounterViewModel
    private var adpater : PrevScoreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        val calendar = Calendar.getInstance()
        val lm = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        prevScoreList.layoutManager = lm
        initModel(calendar, binding)
        binding.counterRoundButton.setOnCounterClick(object : CounterRoundButton.OnCounterClickListener {
            override fun onCounterClick(counter: Int) {
                model.updateScore(counter)
            }
        })
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
            R.id.edit_mode_enabled -> {
                item!!.isChecked = item.isChecked.not()
                App.instance.editMode = item.isChecked
                adpater?.notifyDataSetChanged()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initModel(calendar: Calendar, binding: ActivityMainBinding) {
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        model = ViewModelProviders.of(this, ScoreCounterViewModelFactory(Date())).get(ScoreCounterViewModel::class.java)
        model.score.observe(this, android.arch.lifecycle.Observer {
            Log.d("[KGB]", "load data : $it")
            binding.counterRoundButton.counter = it?.score ?: 0
        })
        model.lastScores.observe(this, android.arch.lifecycle.Observer {
            Log.d("[KGB]", "load data : $it")
            if (adpater == null) {
                adpater = PrevScoreAdapter(model)
                prevScoreList.adapter = adpater
            }
            adpater?.notifyDataSetChanged()
        })
    }

    private fun clearAll() {
        model.clearIncorrectData()
    }
}
