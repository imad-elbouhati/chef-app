package com.majjane.chefmajjane.views

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.views.activities.HomeActivity


class LanguageFragment : Fragment(R.layout.fragment_language) {
    override fun onResume() {
        super.onResume()
        ((activity as HomeActivity)).apply {
            setToolbar(getString(R.string.lang))
            setToolbarHeight(170)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<Button>(R.id.frenchBtn)

    }


}