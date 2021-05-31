package com.majjane.chefmajjane.views

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.majjane.chefmajjane.R
import de.hdodenhof.circleimageview.CircleImageView


class HomeActivity : AppCompatActivity() {
    private var toolBar: Toolbar? = null
    private var toolbarIcon: ImageView? = null
    private var toolbarTitle: TextView? = null
    private var circleImageView:CircleImageView?=null
    var bigCircleImageView:CircleImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolBar = findViewById(R.id.toolbar)
        toolbarIcon = findViewById(R.id.toolbarIcon)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        circleImageView = findViewById(R.id.profileImageView)
        bigCircleImageView = findViewById(R.id.bigProfileImageView)

    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //Soft-Keyboard
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setToolbar(title: String) {
        toolbarTitle?.text = title
    }
    fun setHeaderVisibility(visibility:Boolean){
        val layout: ConstraintLayout = findViewById(R.id.constraintLayout)
        layout.visibility = if(visibility) View.VISIBLE else View.GONE
    }
    fun setImageVisibility(visibility: Boolean){
        circleImageView?.visibility = if(visibility) View.VISIBLE else View.GONE
    }
    fun setToolbarHeight(height: Int) {

        val layoutParams:ConstraintLayout.LayoutParams = toolBar!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = height.toDips().toInt()
        toolBar!!.layoutParams = layoutParams
    }
    fun Int.toDips() =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics)
}