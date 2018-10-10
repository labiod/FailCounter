package com.kgb.failcounter.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.TextView
import com.kgb.failcounter.R

class CounterRoundButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _counter: Int = 0
    var counter : Int
        get() = _counter
        set(value) {
            _counter = value
            refreshView()
        }

    private val counterButton: ImageButton
    private val counterTextView: TextView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val root = inflate(context, R.layout.round_counter_button_layout, this)
        counterButton = root.findViewById(R.id.counterButton)
        counterTextView = root.findViewById(R.id.counterTextView)
        counterButton.setOnClickListener {
            increaseCounter()
        }
        val typeArr = context.obtainStyledAttributes(attrs, R.styleable.CounterRoundButton)
        counter = typeArr.getInt(R.styleable.CounterRoundButton_counter, 0)
        typeArr.recycle()
        refreshView()
    }

    private fun increaseCounter() {
        counter++
        refreshView()
    }

    private fun refreshView() {
        post {
            counterTextView.text = resources.getString(R.string.counter_text_format, counter)
        }
    }
}