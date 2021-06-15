package com.majjane.chefmajjane.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentProfileMenuBinding
import com.majjane.chefmajjane.utils.SharedPreferencesHandler
import com.majjane.chefmajjane.utils.startNewActivity
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.activities.LoginActivity


class ProfileMenuFragment : Fragment(R.layout.fragment_profile_menu) {
    private lateinit var navController: NavController
    private  var _binding : FragmentProfileMenuBinding?=null
    private val binding get() = _binding!!
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            toolbarIcon?.setImageResource(R.drawable.back_arrow_ic)
            setToolbarHeight(50)
            this.setToolbar("")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.myProfile.setOnClickListener {
            navController.navigate(R.id.action_profileMenuFragment_to_myProfileFragment)
        }
        binding.reclamation.setOnClickListener {
            navController.navigate(R.id.action_profileMenuFragment_to_reclamationFragment)
        }
        binding.contact.setOnClickListener {
            navController.navigate(R.id.action_profileMenuFragment_to_contactFragment)
        }
        binding.language.setOnClickListener {
            navController.navigate(R.id.action_profileMenuFragment_to_languageFragment)
        }

        binding.logout.setOnClickListener {
            SharedPreferencesHandler(requireContext()).saveIdCustomer(-1)
            requireActivity().startNewActivity(LoginActivity::class.java)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}