package com.majjane.chefmajjane.views

import android.os.BaseBundle
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
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

    val adapter by lazy {
        CommandeAdapter()
    }
    var articleList: Array<Article>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleList = requireArguments().getParcelableArray(ARTICLE_LIST_BUNDLE) as Array<Article>?
    }

    private val TAG = "MesCommandesFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ${articleList?.toList().toString()}")
        initRecyclerView()

        var totalSum = 0.0
        articleList!!.forEach { article -> totalSum += article.prixTTC*article.selectedQuantity }
        binding.apply {
            articleTotalPrice.text = totalSum.toString()+ " MAD"
            deliveryCost.text = "10 MAD"
            val total =  totalSum + 10 // todo: Change Delivery Cost !!
            totalPrice.text = total.toString() + " MAD"
        }
        if(articleList!!.size > 3 ){
            binding.displayAll.apply {
                visible(true)
                setOnClickListener {
                    // todo : display all commandes
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter.setItems(articleList!!.toList())
        binding.recyclerView.adapter = adapter
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMesCommandesBinding = FragmentMesCommandesBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<CommandeViewModel> = CommandeViewModel::class.java
    override fun getFragmentRepository(): CommandeRepository =
        CommandeRepository(RemoteDataSource().buildApi(CommandeApi::class.java))
}