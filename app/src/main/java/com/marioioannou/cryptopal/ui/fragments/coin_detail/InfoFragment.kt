package com.marioioannou.cryptopal.ui.fragments.coin_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentHomeBinding
import com.marioioannou.cryptopal.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}