package com.majjane.chefmajjane.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.HomeActivity
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentMesCommandesBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.viewmodel.CategoryViewModel
import com.majjane.chefmajjane.views.base.BaseFragment


class MesCommandesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_mes_commandes, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).apply {
            setToolbar("Mes Commandes")
            setToolbarHeight(50)
            setVisible(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}