package com.majjane.chefmajjane.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.adapters.CommandeAdapter
import com.majjane.chefmajjane.databinding.FragmentMesCommandesBinding
import com.majjane.chefmajjane.network.CommandeApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.CommandeRepository
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_LIST_BUNDLE
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.CommandeViewModel
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


@Suppress("UNCHECKED_CAST")
class MesCommandesFragment :
    BaseFragment<CommandeViewModel, FragmentMesCommandesBinding, CommandeRepository>() {

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).apply {
            setToolbar("Mes Commandes")
            setToolbarHeight(50)
            setVisible(true)
        }
    }

    private val adapter by lazy {
        CommandeAdapter()
    }
    var articleList: Array<Article>? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bundle.getParcelableArray(ARTICLE_LIST_BUNDLE)?.let {
                articleList = it as Array<Article>?
            }
        }
        activity?.run {
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }
    }

    private val TAG = "MesCommandesFragment"
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        sharedViewModel.commandList.observe(viewLifecycleOwner, {
//            Log.d(TAG, "onViewCreated: ${it.size}")
            initRecyclerView(it)
            var totalSum = 0.0
            it?.let { articleList ->
                articleList.forEach { article -> totalSum += article.prixTTC * article.selectedQuantity }
                binding.apply {
                    articleTotalPrice.text = totalSum.toString() + " MAD"
                    deliveryCost.text = "10 MAD"
                    val total = totalSum + 10 // todo: Change Delivery Cost !!
                    totalPrice.text = total.toString() + " MAD"
                }
                if (articleList.size > 3) {
                    binding.displayAll.apply {
                        visible(true)
                        setOnClickListener {
                            navController.navigate(R.id.action_mesCommandesFragment_to_displayCommandsFragment)
                        }
                    }
                }
            }
        })


        binding.locationRow.setOnClickListener {
            navController.navigate(R.id.action_mesCommandesFragment_to_mapsFragment)
        }
        sharedViewModel.address.observe(viewLifecycleOwner, {
            binding.addressText.text = it.address.toString()
        })
    }

    private fun calculateTotal() {

    }

    private fun initRecyclerView(list: List<Article>) {
        list?.let {
            adapter.setItems(it.toList())
            binding.recyclerView.adapter = adapter
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMesCommandesBinding = FragmentMesCommandesBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<CommandeViewModel> = CommandeViewModel::class.java
    override fun getFragmentRepository(): CommandeRepository =
        CommandeRepository(RemoteDataSource().buildApi(CommandeApi::class.java))
}