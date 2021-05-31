package com.majjane.chefmajjane.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.Sushi
import com.majjane.chefmajjane.adapters.SushiAdapter
import com.majjane.chefmajjane.databinding.FragmentEspaceSushiBinding
import com.majjane.chefmajjane.databinding.FragmentLoginBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class EspaceSushiFragment :
    BaseFragment<AuthViewModel, FragmentEspaceSushiBinding, AuthRepository>() {
    private val adapter by lazy {
        SushiAdapter()
    }

    private val TAG = "EspaceSushiFragment"

    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).setToolbar("Espace Sushi")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sushiList = listOf(
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "AAaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            ),
            Sushi(
                "Ramen Naruto",
                "Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo Aaz depe kdeo edeo ",
                "70,00 MAD"
            )
        )

        binding.sushiRecyclerView.adapter = adapter
        adapter.setItems(sushiList)


        binding.sushiRecyclerView.startLayoutAnimation()


    }


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEspaceSushiBinding = FragmentEspaceSushiBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AuthViewModel> = AuthViewModel::class.java


    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(RemoteDataSource().buildApi(AuthApi::class.java))

}