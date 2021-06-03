package com.majjane.chefmajjane

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var article: Article? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        article = requireArguments().getParcelable(ARTICLE_BUNDLE)
    }

    private val TAG = "FoodDetailsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if (article?.selectedQuantity!! > 0) {
            binding.totalSumButton.visible(true)
            binding.totalSumButton.text = "Ajouter ${binding.counterView2.getQuantity()} au panier  ${article?.prixTTC?.times(binding.counterView2.getQuantity())} MAD"
        }
        binding.counterView2.setQuantityChangedListener(object :
            CounterView.QuantityChangedListener {
            override fun onQuantityChanged(article: Article?) {
                if (article?.selectedQuantity!! > 0) {
                    binding.totalSumButton.visible(true)
                    binding.totalSumButton.text = "Ajouter ${binding.counterView2.getQuantity()} au panier  ${article?.prixTTC?.times(binding.counterView2.getQuantity())} MAD"
                }else{
                    binding.totalSumButton.visible(false)
                }
            }
        })


    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSushiDetailsBinding = FragmentSushiDetailsBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java

    override fun getFragmentRepository(): AccueilMenuRepository = AccueilMenuRepository(
        RemoteDataSource().buildApi(AccueilMenuApi::class.java)
    )
}