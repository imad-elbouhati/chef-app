package com.majjane.chefmajjane.views

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.FacebookSdk.getApplicationContext
import com.majjane.chefmajjane.adapters.SushiAdapter
import com.majjane.chefmajjane.databinding.FragmentEspaceSushiBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.AccueilResponseItem
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.utils.Constants.Companion.CATEGORY_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment
import java.util.*


class EspaceSushiFragment :
    BaseFragment<AccueilMenuViewModel, FragmentEspaceSushiBinding, AccueilMenuRepository>() {
    private val adapter by lazy {
        SushiAdapter({ food, position -> onFoodClicked(food, position) }, { sum, articleHashMap ->
            onTotalPriceChangedListener(
                sum,
                articleHashMap
            )
        })
    }

    private fun onTotalPriceChangedListener(sum: Float, articleHashMap: HashMap<Int, Article>) {

        if (sum > 0) {
            binding.totalSumButton.apply {
                visible(true)
                text = "Commander ${articleHashMap.size} pour $sum MAD "
            }
        } else {
            binding.totalSumButton.visible(false)
        }
    }

    private fun onFoodClicked(foodResponse: Article, position: Int) {
        Log.d(TAG, "onFoodClicked: ${foodResponse.name}")
    }

    private val TAG = "EspaceSushiFragment"
    private lateinit var categoryArgs: AccueilResponseItem
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar("Espace Sushi")
            toolbarIcon?.visible(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryArgs = requireArguments().getParcelable(CATEGORY_BUNDLE)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: $categoryArgs")
        initRecyclerView()
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.foodListResponse.observe(viewLifecycleOwner, { it ->
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar2.visible(true)
                    isLoading = true
                    Log.d(TAG, "onViewCreated: Loading...")
                }
                is Resource.Success -> {
                    isLoading = false
                    binding.progressBar2.visible(false)
                    val totalPages = it.data.total_products / QUERY_PAGE_SIZE + 2
                    isLastPage = viewModel.searchFoodPage == totalPages
                    adapter.setItems(it.data.articles.filter { article ->
                        article.qnt > 0
                    })
                }
                is Resource.Failure -> {
                    binding.progressBar2.visible(false)
                    handleApiError(it) {
                        getFoodList()
                    }
                }
            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage

            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0

            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
            isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getFoodList(1, categoryArgs.id)
                isScrolling = false
            }
        }
    }

    private fun initRecyclerView() {
        binding.sushiRecyclerView.adapter = adapter
        binding.sushiRecyclerView.startLayoutAnimation()
        binding.sushiRecyclerView.addOnScrollListener(this@EspaceSushiFragment.scrollListener)
    }

    private fun getFoodList() {
        viewModel.getFoodList(idLang = 1, categoryArgs.id)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEspaceSushiBinding = FragmentEspaceSushiBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java


    override fun getFragmentRepository(): AccueilMenuRepository =
        AccueilMenuRepository(RemoteDataSource().buildApi(AccueilMenuApi::class.java))

}