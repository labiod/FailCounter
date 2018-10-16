package com.kgb.failcounter.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.kgb.failcounter.App
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
            holder.binder.editModeButton.visibility = if (App.instance.editMode) View.VISIBLE else View.GONE
            holder.binder.editView.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            holder.binder.editView.wrapSelectorWheel = false
            holder.binder.editModeButton.setOnClickListener { _ ->
                if (holder.binder.editView.visibility == View.GONE) {
                    holder.binder.editView.visibility = View.VISIBLE
                    holder.binder.scoreTextView.visibility = View.GONE
                    holder.binder.editView.minValue = 0
                    holder.binder.editView.maxValue = 100
                    holder.binder.editView.value = item.score
                    holder.binder.editModeButton.setImageResource(R.drawable.score_edit_confirm)
                } else {
                    holder.binder.scoreTextView.visibility = View.VISIBLE
                    holder.binder.editView.visibility = View.GONE
                    holder.binder.editModeButton.setImageResource(R.drawable.score_edit_drawable)
                    model.updateItem(item, holder.binder.editView.value)
                }
            }
            holder.binder.date = DateUtil.getDateInFormat(it.date, "MMM d, ''yy")
        }

    }

    private fun getItem(position: Int): ScoreCounterEntity? {
        return model.lastScores.value?.get(position)
    }

    class Holder(val binder: PrevScoreItemLayoutBinding) : RecyclerView.ViewHolder(binder.root)
}



