package com.majjane.chefmajjane

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.majjane.chefmajjane.databinding.FragmentSushiDetailsBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_BUNDLE
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.views.base.BaseFragment
import com.majjane.chefmajjane.views.customviews.CounterView


class FoodDetailsFragment :
    BaseFragment<AccueilMenuViewModel, FragmentSushiDetailsBinding, AccueilMenuRepository>() {
    private var navController:NavController?=null
    private var article: Article? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        article = requireArguments().getParcelable(ARTICLE_BUNDLE)
    }

    private val TAG = "FoodDetailsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.apply {
            article?.let { counterView2.setModel(it) }
            counterView2.setQuantity(article?.selectedQuantity.toString())
            decriptinTextView.text = article?.description
            foodName.text = article?.name
            foodPrice.text = article?.prixTTC.toString() + " MAD"
            Glide.with(this@FoodDetailsFragment)
                .load(article?.image)
                .into(this.sushiImageView)

        }
     //   onBackPressed()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSushiDetailsBinding = FragmentSushiDetailsBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java

    override fun getFragmentRepository(): AccueilMenuRepository = AccueilMenuRepository(
        RemoteDataSource().buildApi(AccueilMenuApi::class.java)
    )
    private fun onBackPressed() {
        val bundle = bundleOf("from_details" to true)
        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                navController?.navigate(R.id.action_sushiDetailsFragment_to_espaceSushiFragment, bundle)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}