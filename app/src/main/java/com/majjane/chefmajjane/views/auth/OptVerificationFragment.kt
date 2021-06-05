package com.majjane.chefmajjane.views.auth

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.majjane.chefmajjane.databinding.FragmentOptVerificationBinding
import com.majjane.chefmajjane.utils.visible


class OptVerificationFragment : Fragment() {
    private var _binding: FragmentOptVerificationBinding? = null
    private val binding get() = _binding!!
    var num = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOptVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val TAG = "OptVerificationFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customKeyBoardListener()
        binding.firstPinView.doOnTextChanged { text, start, before, count ->
            text.let {
               if(it.toString().length==5){
                   val code = num
                   binding.progressBar.visible(true)
                   
               }
            }

        }

    }

    private fun customKeyBoardListener() {
        binding.numOne.setOnClickListener {
            num +="1"
            binding.firstPinView.setText(num)
        }
        binding.numTwo.setOnClickListener {
            num +="2"
            binding.firstPinView.setText(num)
        }
        binding.numTree.setOnClickListener {
            num +="3"
            
            binding.firstPinView.setText(num)
        }
        binding.numFour.setOnClickListener {
            num +="4"
            
            binding.firstPinView.setText(num)
        }

        binding.numFive.setOnClickListener {
            num +="5"
            
            binding.firstPinView.setText(num)
        }

        binding.numSix.setOnClickListener {
            num +="6"
            
            binding.firstPinView.setText(num)
        }

        binding.numSeven.setOnClickListener {
            num +="7"
            
            binding.firstPinView.setText(num)
        }
        binding.numEight.setOnClickListener {
            num +="8"
            
            binding.firstPinView.setText(num)
        }
        binding.numNine.setOnClickListener {
            num +="9"
            
            binding.firstPinView.setText(num)
        }
        binding.numZero.setOnClickListener {
            num +="0"

            binding.firstPinView.setText(num)
        }
        binding.deleteIcon.setOnClickListener {
            num = num.dropLast(1)
            
            binding.firstPinView.setText(num)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}