package com.majjane.chefmajjane

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.chaos.view.PinView
import com.majjane.chefmajjane.databinding.FragmentOptVerificationBinding
import java.lang.StringBuilder


class OptVerificationFragment : Fragment() {
    private var _binding: FragmentOptVerificationBinding? = null
    private val binding get() = _binding!!
    var num = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOptVerificationBinding.inflate(inflater,container,false)
        return binding.root
    }
    private val TAG = "OptVerificationFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customKeyBoardListener()
        binding.firstPinView.doOnTextChanged { text, start, before, count ->
            Log.d(TAG, "onViewCreated: $text")
            text.let {
               if(it.toString().length==5){

               }
            }

        }
    }

    private fun customKeyBoardListener() {
        binding.numOne.setOnClickListener {
            num +="1"
            Log.d(TAG, "customKeyBoardListener: $num")
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