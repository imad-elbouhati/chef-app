package com.majjane.chefmajjane.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.adapters.MenuAdapter
import com.majjane.chefmajjane.adapters.FoodAdapter
import com.majjane.chefmajjane.databinding.FragmentEspaceSushiBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.AccueilResponseItem
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.responses.menu.MenuResponseItem
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.CATEGORY_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment
import java.util.*
import kotlin.collections.HashMap


class FoodListFragment :
    BaseFragment<AccueilMenuViewModel, FragmentEspaceSushiBinding, AccueilMenuRepository>() {
    private lateinit var mArticleHashMap: HashMap<Int, Article>
    private val adapter by lazy {
        FoodAdapter({ food, position -> onFoodClicked(food, position) }, { sum, articleHashMap ->
            onTotalPriceChangedListener(
                sum,
                articleHashMap
            )
        })
    }
    private val menuAdapter by lazy {
        MenuAdapter { menu, position -> onMenuClicked(menu, position) }
    }

    private fun onMenuClicked(menu: MenuResponseItem, position: Int) {
        isSearchingMenu = true
        menuId = menu.id
        viewModel.searchFoodPage = 0
        viewModel.nextFoodListResponse = null
        mArticleHashMap = HashMap()
        mArticleHashMap.clear()
        Log.d(TAG, "onMenuClicked: $mArticleHashMap")
        binding.totalSumButton.visible(false)
        // viewModel.getFoodByMenuList(id_lang=1,menu.id)
        viewModel.getFoodList(idLang = 1, menu.id)

    }

    private val TAG = "FoodListFragment"
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

    private var navController: NavController? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // getMenuList(1, 121)
        navController = Navigation.findNavController(view)
        initRecyclerView()
        if (menuId == 0) {
            getFoodList(idLang = 1, categoryArgs.id)
            getMenuList(idLang = 1, categoryArgs.id)
        } else {
            getFoodList(idLang = 1, menuId)
            getMenuList(idLang = 1, 121)
        }
        binding.totalSumButton.setOnClickListener {
            mArticleHashMap.forEach { t, u ->
                Log.d(TAG, "onTotalSumBtnClicked: ${u.name} ${u.selectedQuantity}")
            }
        }

        observeFoodListResponse()
        observerMenuListResponse()

    }

    private fun observerMenuListResponse() {
        viewModel.menuListResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar2.visible(true)

                }
                is Resource.Success -> {
                    binding.progressBar2.visible(false)

                    menuAdapter.setItems(it.data)
                }
                is Resource.Failure -> {
                    binding.progressBar2.visible(false)
                    handleApiError(it) {
                        getMenuList(1, 121)
                    }
                }
            }
        })
    }

    private fun getMenuList(idLang: Int, i: Int) {
        viewModel.getMenuList(idLang = 1, i)
    }

    private fun observeFoodListResponse() {
        viewModel.foodListResponse.observe(viewLifecycleOwner, { it ->
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar2.visible(true)
                    isLoading = true
                }
                is Resource.Success -> {
                    isLoading = false
                    binding.progressBar2.visible(false)
                    val totalPages = it.data.total_products / QUERY_PAGE_SIZE + 2
                    isLastPage = viewModel.searchFoodPage == totalPages
                    adapter.setItems(it.data.articles)
                }
                is Resource.Failure -> {
                    binding.progressBar2.visible(false)
                    handleApiError(it) {
                        getFoodList(1, menuId)
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
//                if(!isSearchingMenu){
//                    viewModel.getFoodList(1, categoryArgs.id)
//                }else{
//                    viewModel.getFoodByMenuList(1, menuId)
//                }
                viewModel.getFoodList(1, menuId)
                isScrolling = false
            }
        }
    }
    var menuId = 0
    private fun initRecyclerView() {
        binding.sushiRecyclerView.adapter = adapter
        binding.sushiRecyclerView.startLayoutAnimation()
        binding.sushiRecyclerView.addOnScrollListener(this@FoodListFragment.scrollListener)
        binding.menuTypeRecyclerView.adapter = menuAdapter
    }

    private fun getFoodList(idLang: Int, i: Int) {
        viewModel.getFoodList(idLang = 1, i)
    }

    private fun onTotalPriceChangedListener(sum: Float, articleHashMap: HashMap<Int, Article>) {
        this.mArticleHashMap = articleHashMap
        if (mArticleHashMap.size > 0) {
            Log.d(TAG, "onTotalPriceChangedListener: ${this.mArticleHashMap}")
            binding.totalSumButton.apply {
                visible(true)
                text = "Commander ${mArticleHashMap.size} pour $sum MAD "
            }
        } else {
            binding.totalSumButton.visible(false)
        }
    }

    private fun onFoodClicked(article: Article, position: Int) {
        val bundle = bundleOf(ARTICLE_BUNDLE to article)
        navController?.navigate(R.id.action_espaceSushiFragment_to_sushiDetailsFragment, bundle)

    }

    var isSearchingMenu = false
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEspaceSushiBinding = FragmentEspaceSushiBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java


    override fun getFragmentRepository(): AccueilMenuRepository =
        AccueilMenuRepository(RemoteDataSource().buildApi(AccueilMenuApi::class.java))

}