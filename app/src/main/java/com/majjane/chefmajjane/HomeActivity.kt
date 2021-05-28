package com.majjane.chefmajjane

import android.opengl.Visibility
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.security.AccessController.getContext


class HomeActivity : AppCompatActivity() {
    private var toolBar: Toolbar? = null
    private var toolbarIcon: ImageView? = null
    private var toolbarTitle: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolBar = findViewById(R.id.toolbar)
        toolbarIcon = findViewById(R.id.toolbarIcon)
        toolbarTitle = findViewById(R.id.toolbarTitle)
    }

    fun setToolbar(title: String) {
        toolbarTitle?.text = title
    }
    fun setHeaderVisibility(visibility:Boolean){
        val layout: ConstraintLayout = findViewById(R.id.constraintLayout)
        layout.visibility = if(visibility) View.VISIBLE else View.GONE
    }
    fun setToolbarHeight(height: Int) {

        val layoutParams:ConstraintLayout.LayoutParams = toolBar!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = height.toDips().toInt()
        toolBar!!.layoutParams = layoutParams
    }
    fun Int.toDips() =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics)
}