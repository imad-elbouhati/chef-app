package com.majjane.chefmajjane.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.views.activities.HomeActivity

class ReclamationFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar(getString(R.string.reclamation))
            setToolbarHeight(170)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reclamation, container, false)
    }

}