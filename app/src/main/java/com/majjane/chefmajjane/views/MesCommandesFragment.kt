package com.majjane.chefmajjane.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.adapters.CommandeAdapter
import com.majjane.chefmajjane.databinding.FragmentMesCommandesBinding
import com.majjane.chefmajjane.network.CommandeApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.CommandeRepository
import com.majjane.chefmajjane.responses.Article
import com.majjane.chefmajjane.responses.cityresponse.City
import com.majjane.chefmajjane.responses.model.CommandeModel
import com.majjane.chefmajjane.responses.model.Product
import com.majjane.chefmajjane.utils.Constants.Companion.ARTICLE_LIST_BUNDLE
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.CommandeViewModel
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment
import kotlin.math.roundToLong


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

        //getCites and delivery cost
        viewModel.getCitiesList(preferences.getIdLang(), preferences.getIdCustomer())
        binding.cityRow.setOnClickListener {
            showCitiesListDialog()
        }

        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.locationRow.setOnClickListener {
            navController.navigate(R.id.action_mesCommandesFragment_to_mapsFragment)
        }
        sharedViewModel.address.observe(viewLifecycleOwner, {
            binding.addressText.text = it.address.toString()
        })
        binding.paimentRow.setOnClickListener {
            showPaimentMethodListDialog()
        }

        binding.confirmerCommandeButton.setOnClickListener {
            if (binding.cityText.text.isEmpty()) {
                requireView().snackbar(getString(R.string.plase_select_city))
                return@setOnClickListener
            }
            if (binding.addressText.text.isEmpty()) {
                requireView().snackbar(getString(R.string.please_select_address))
                return@setOnClickListener
            }
            if (binding.methodePaimentText.text.isEmpty()) {
                requireView().snackbar(getString(R.string.please_select_payment_method))
                return@setOnClickListener
            }
            if (binding.phoneNumberEditText.text.isEmpty()) {
                requireView().snackbar(getString(R.string.please_type_phone_number))
                return@setOnClickListener
            }
            val productList = mutableListOf<Product>()
            mArticleList?.let {
                for (article in it) {
                    productList.add(Product(article.id, article.selectedQuantity))
                }
            }

            if (selectedMethodModel?.id == 1) {
                mSelectedCity?.id?.let { selectedCity ->

                    viewModel.sendCommandeToApi(
                        CommandeModel(
                            binding.addressText.text.toString(),
                            selectedCity,
                            preferences.getIdCustomer(),
                            1,
                            binding.phoneNumberEditText.text.toString(),
                            "",
                            productList,
                            ""
                        )
                    )
                }
            }
        }
        observeIsCommandeConfirmed()
        observeCommandList()
        observeCitiesList()
    }

    private fun observeIsCommandeConfirmed() {
        viewModel.confirmCommandResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar10.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar10.visible(false)
                    requireView().snackbar(getString(R.string.went_wrong))
                }
                is Resource.Success -> {
                    binding.progressBar10.visible(false)
                    if (it.data.success == 1) {
                        requireView().snackbar(getString(R.string.commande_send_succefully))
                        navController.navigate(R.id.action_mesCommandesFragment_to_homeFragment)
                    }
                    if (it.data.success == 0) {
                        requireView().snackbar(it.data.message)
                    }

                }
            }
        })
    }

    private fun observeCitiesList() {
        viewModel.citiesListResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Failure -> {
                    findNavController().popBackStack()
                }
                is Resource.Success -> {
                    mCitiesList = it.data.cities.toList() as ArrayList<City>
                    it.data.phone_number?.let { phoneNumber ->
                        binding.phoneNumberEditText.setText(phoneNumber)
                    }
                }
            }
        })
    }

    private var mCitiesList = mutableListOf<City>()
    private var mSelectedCity: City? = null
    private fun showCitiesListDialog() {

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_singlechoice, mCitiesList)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.pick_city)
        builder.setAdapter(adapter) { dialog, which ->
            mSelectedCity = mCitiesList[which]
            val selectedCity = mCitiesList[which].name
            binding.cityText.text = selectedCity
            mDeliveryCost = mCitiesList[which].price
            binding.deliveryCost.text = String.format("%.2f %s", mCitiesList[which].price, "MAD")
            // total sum of articles +  delivery cost
            val total = totalSum.plus(mDeliveryCost)
            binding.totalPrice.text = String.format("%.2f %s", total, "MAD")
        }
        builder.show()
    }

    private var selectedMethodModel: MethodPaiment? = null
    private fun showPaimentMethodListDialog() {
        val paimentMethodList = listOf(
            MethodPaiment(1, getString(R.string.cash_on_delivery)),
            MethodPaiment(2, getString(R.string.payment_by_card))
        )
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_singlechoice,
            paimentMethodList
        )
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.pick_city)
        builder.setAdapter(adapter) { dialog, which ->
            selectedMethodModel = paimentMethodList[which]
            val selectedMethod = paimentMethodList[which].methodName
            binding.methodePaimentText.text = selectedMethod

        }
        builder.show()
    }

    private var mDeliveryCost: Double = 0.0
    private var mArticleList: List<Article>? = null
    var totalSum = 0.0
    private fun observeCommandList() {
        totalSum = 0.0
        sharedViewModel.commandList.observe(viewLifecycleOwner, {
            mArticleList = it
            initRecyclerView(it)

            it?.let { articleList ->
                articleList.forEach { article -> totalSum += article.prixTTC * article.selectedQuantity }
                binding.apply {
                    articleTotalPrice.text = totalSum.toString() + " MAD"
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

    data class MethodPaiment(val id: Int, val methodName: String) {
        override fun toString(): String {
            return methodName
        }
    }

}