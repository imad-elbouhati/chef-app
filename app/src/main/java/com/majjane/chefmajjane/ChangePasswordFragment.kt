package com.majjane.chefmajjane

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.views.activities.HomeActivity

class ChangePasswordFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).setToolbar(getString(R.string.change_password))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

}