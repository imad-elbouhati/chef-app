package com.majjane.chefmajjane.views.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.responses.Article

class CounterView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val TAG = "CounterView"
    var plusButton: Button? = null
    var minusButton: Button? = null
    var quantityTextView: TextView? = null
    var article: Article? = null
    var quantityChangedListener:QuantityChangedListener?=null
    interface QuantityChangedListener {
        fun onQuantityChanged(article: Article?)
    }
    @JvmName("setQuantityChangedListener1")
    fun setQuantityChangedListener(quantityChangedListener:QuantityChangedListener){
        this.quantityChangedListener = quantityChangedListener
    }
    fun setModel(article: Article) {
        Log.d(TAG, "setModel: $article")
        this.article = article
    }

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
            if(article!=null){
                if(article!!.selectedQuantity < article!!.qnt){
                    article?.selectedQuantity = article?.selectedQuantity?.plus(1)!!
                    quantityTextView!!.text = article?.selectedQuantity.toString()
                }
            }
        }


        minusButton!!.setOnClickListener {
            if (article?.selectedQuantity!! > 0) {
                article?.selectedQuantity = article?.selectedQuantity?.minus(1)!!
                quantityTextView!!.text = article?.selectedQuantity.toString()
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
                quantityChangedListener?.onQuantityChanged(article)

            }


            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }


    fun setQuantity(quantity: String) {
        quantityTextView?.text = quantity
    }

}
