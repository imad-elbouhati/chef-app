package com.majjane.chefmajjane.views.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.majjane.chefmajjane.R

class CounterView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var plusButton: Button? = null
    var minusButton: Button? = null
    var quantityTextView: TextView? = null
    var num = 0

    init {
        inflate(context, R.layout.counter_view, this)
        plusButton = findViewById(R.id.plusBtn)
        minusButton = findViewById(R.id.minusBtn)
        quantityTextView = findViewById(R.id.quantityTV)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CounterView)
        quantityTextView!!.text = attributes.getString(R.styleable.CounterView_quantity_text)

        highlightHandler()
        buttonsClickListener()

        attributes.recycle()
    }

    private fun buttonsClickListener() {
        plusButton!!.setOnClickListener {
            num += 1
            quantityTextView!!.text = num.toString()
        }


        minusButton!!.setOnClickListener {
            if (num > 0) {
                num -= 1
                quantityTextView!!.text = num.toString()
            }
        }
    }

    private fun highlightHandler() {
        quantityTextView!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().toInt() >= 1) {
                    plusButton!!.setBackgroundResource(R.drawable.ic_baseline_filled_add_circle_24)
                    minusButton!!.setBackgroundResource(R.drawable.ic_outline_remove_circle_outline_24)

                } else {
                    plusButton!!.setBackgroundResource(R.drawable.ic_baseline_add_circle_outline_24)
                    minusButton!!.setBackgroundResource(R.drawable.ic_baseline_remove_circle_outline_gray_24)

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    fun getQuantity():Int{
        return quantityTextView!!.text.toString().toInt()
    }
}