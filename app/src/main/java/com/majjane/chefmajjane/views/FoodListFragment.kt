package com.majjane.chefmajjane.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.adapters.MenuAdapter
import com.majjane.chefmajjane.adapters.SushiAdapter
import com.majjane.chefmajjane.databinding.FragmentEspaceSushiBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.AccueilResponseItem
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.responses.menu.MenuResponseItem
import com.majjane.chefmajjane.utils.*
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_LIST_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.CATEGORY_BUNDLE
import com.majjane.chefmajjane.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.activities.LoginActivity
import com.majjane.chefmajjane.views.base.BaseFragment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FoodListFragment :
    BaseFragment<AccueilMenuViewModel, FragmentEspaceSushiBinding, AccueilMenuRepository>() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var categoryArgs: AccueilResponseItem

    private val adapter by lazy {
        SushiAdapter({ food, position -> onFoodClicked(food, position) }, { sum, articleHashMap ->
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
        //adapter.articleHashMap.clear()

        viewModel.getFoodList(idLang = 1, menu.id)
    }

    private val TAG = "FoodListFragment"
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar("Espace Sushi")
            toolbarIcon?.setImageResource(R.drawable.back_arrow_ic)
            toolbarIcon?.visible(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let { bundle ->
//            bundle.getParcelable<AccueilResponseItem>(CATEGORY_BUNDLE)?.let {
//                categoryArgs = it
//                sharedViewModel
//            }
//        }
        activity?.run {
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }


    }

    private var navController: NavController? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        sharedViewModel.sharedCategory.observe(viewLifecycleOwner, {
            categoryArgs = it
            if (menuId == 0) {
                getFoodList(preferences.getIdLang(), categoryArgs.id)
                getMenuList(preferences.getIdLang(), categoryArgs.id)
            } else {
                getFoodList(preferences.getIdLang(), menuId)
                getMenuList(preferences.getIdLang(), 121)
            }
        })
        initRecyclerView()

        //Check whether the articleHashMap is empty or not to show the button every time the fragment been created
        adapter.articleHashMap?.let {
            if (it.size > 0) {
                binding.totalSumButton.apply {
                    text =
                        getString(R.string.commander) + " ${it.size} " + getString(R.string.pour) + " pour ${adapter.totalPrice} MAD"
                    visible(true)
                }
            }
        }


        //On Total Button Clicked Send Array in a bundle to CammandeFragment
        binding.totalSumButton.setOnClickListener {
            val articleList = ArrayList(mArticleHashMap?.values).toTypedArray()
            navController?.navigate(
                R.id.action_espaceSushiFragment_to_mesCommandesFragment,
                bundleOf(ARTICLE_LIST_BUNDLE to articleList)
            )
            sharedViewModel.commandList.value = articleList.toList()
        }
        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            navController?.navigate(R.id.action_espaceSushiFragment_to_homeFragment)
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
                        getMenuList(preferences.getIdLang(), 121)
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
                    adapter.setItems(it.data.articles as ArrayList<Article>)
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
                isScrolling = if (menuId == 0) {
                    viewModel.getFoodList(1, categoryArgs.id)
                    false
                } else {
                    viewModel.getFoodList(1, menuId)
                    false
                }
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

    var mArticleHashMap: HashMap<Int, Article>? = null
    private fun onTotalPriceChangedListener(sum: Float, articleHashMap: HashMap<Int, Article>) {
        //Check if user connected
        if (preferences.getIdCustomer() != -1) {
            if (sum > 0) {
                binding.totalSumButton.apply {
                    visible(true)
                    text = "Commander ${articleHashMap.size} pour $sum MAD "
                }
                this.mArticleHashMap = articleHashMap
            } else {
                binding.totalSumButton.visible(false)
            }
        } else {
            requireActivity().startNewActivity(LoginActivity::class.java)
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






















































