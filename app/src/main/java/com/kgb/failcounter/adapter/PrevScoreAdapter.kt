package com.kgb.failcounter.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kgb.failcounter.DateUtil
import com.kgb.failcounter.R
import com.kgb.failcounter.databinding.PrevScoreItemLayoutBinding
import com.kgb.failcounter.db.ScoreCounterEntity
import com.kgb.failcounter.viewmodel.ScoreCounterViewModel

class PrevScoreAdapter(val model: ScoreCounterViewModel) : RecyclerView.Adapter<PrevScoreAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val binder = DataBindingUtil.inflate<PrevScoreItemLayoutBinding>(LayoutInflater.from(parent.context), R.layout.prev_score_item_layout, parent, false)
        return Holder(binder)
    }

    override fun getItemCount(): Int {
        return model.lastScores.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binder.score = it.score
            holder.binder.date = DateUtil.getDateInFormat(it.date, "MMM d, ''yy")
        }

    }

    private fun getItem(position: Int): ScoreCounterEntity? {
        return model.lastScores.value?.get(position)
    }

    class Holder(val binder: PrevScoreItemLayoutBinding) : RecyclerView.ViewHolder(binder.root)
}