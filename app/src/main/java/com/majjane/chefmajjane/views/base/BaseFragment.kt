package com.majjane.chefmajjane.views.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.majjane.chefmajjane.viewmodel.ViewModelFactory
import com.majjane.chefmajjane.repository.base.BaseRepository


abstract class BaseFragment<VM : ViewModel, V : ViewBinding, R : BaseRepository> : Fragment() {
    private var _binding: V? = null
    protected val binding get() = _binding!!
    lateinit var viewModel: VM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(createViewModel())
        return binding.root
    }


    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): V
    abstract fun createViewModel(): Class<VM>
    abstract fun getFragmentRepository(): R
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}