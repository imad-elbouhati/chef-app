package com.majjane.chefmajjane.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentProfileMenuBinding
import com.majjane.chefmajjane.utils.SharedPreferencesHandler
import com.majjane.chefmajjane.utils.startNewActivity
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.activities.LoginActivity


class ProfileMenuFragment : Fragment(R.layout.fragment_profile_menu) {
    private lateinit var navController: NavController
    private var _binding: FragmentProfileMenuBinding? = null
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
        

        binding.logout.setOnClickListener {
            SharedPreferencesHandler(requireContext()).saveIdCustomer(-1)
            LoginManager.getInstance().logOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()
            requireActivity().startNewActivity(LoginActivity::class.java)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}