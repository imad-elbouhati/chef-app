package com.majjane.chefmajjane.views

import android.os.BaseBundle
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.majjane.chefmajjane.DisplayAllCommandesAdapter
import com.majjane.chefmajjane.databinding.FragmentDisplayCommandsBinding
import com.majjane.chefmajjane.databinding.FragmentMesCommandesBinding
import com.majjane.chefmajjane.network.CommandeApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.CommandeRepository
import com.majjane.chefmajjane.viewmodel.CommandeViewModel
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class DisplayCommandsFragment :
    BaseFragment<CommandeViewModel, FragmentDisplayCommandsBinding, CommandeRepository>() {
    private var navController:NavController?=null
    private val adapter by lazy {
        DisplayAllCommandesAdapter()
    }
    lateinit var sharedViewModel: SharedViewModel
    private val TAG = "DisplayCommandsFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        navController = Navigation.findNavController(view)

        sharedViewModel.commandList.observe(viewLifecycleOwner, {
            adapter.setItems(it.toList())
        })

        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDisplayCommandsBinding =
        FragmentDisplayCommandsBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<CommandeViewModel> = CommandeViewModel::class.java
    override fun getFragmentRepository(): CommandeRepository =
        CommandeRepository(RemoteDataSource().buildApi(CommandeApi::class.java))
}