package com.majjane.chefmajjane.views.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.utils.visible

class CustomTextField(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    var textViewTitle: TextView? = null
    var editText: EditText? = null
    var line: View? = null
    var errorLabel: TextView? = null

    init {
        inflate(context, R.layout.custom_text_field, this)
        initViews()
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTextField)
        textViewTitle?.text = attributes.getString(R.styleable.CustomTextField_textTitle)
        editText?.setCompoundDrawablesWithIntrinsicBounds(attributes.getDrawable(R.styleable.CustomTextField_drawableLeft),null,null,null)
        editText?.setCompoundDrawablesWithIntrinsicBounds(null,null,attributes.getDrawable(R.styleable.CustomTextField_drawableRight),null)
        editText?.inputType = attributes.getInt(R.styleable.CustomTextField_inputType,EditorInfo.TYPE_TEXT_VARIATION_NORMAL)
        editText?.hint = attributes.getString(R.styleable.CustomTextField_hint)
        editText?.setText(attributes.getString(R.styleable.CustomTextField_textt))
        attributes.recycle()

    }

    private fun initViews() {
        textViewTitle = findViewById(R.id.textViewTitle)
        editText = findViewById(R.id.editText)
        line = findViewById(R.id.line)
        errorLabel = findViewById(R.id.errorLabel)
    }

    fun getText():String = editText?.text.toString()

    fun setError(errorText:String){
        line?.setBackgroundColor(resources.getColor(R.color.error_color))
        errorLabel?.visible(true)
        errorLabel?.text = errorText
    }
    fun setText(text:String){
        editText?.setText(text)
    }
    fun setDrawable(resource:Drawable){
        editText?.setCompoundDrawablesWithIntrinsicBounds(resource,null,null,null)
    }


    
}