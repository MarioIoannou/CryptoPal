package com.marioioannou.cryptopal.ui.fragments.coin_detail.filter_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marioioannou.cryptopal.databinding.FragmentCoinFilterBottomSheetBinding
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.viewmodels.MainViewModel

class CoinFilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCoinFilterBottomSheetBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCoinFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            radioGroup.check()
            radioGroup.setOnCheckedChangeListener(
                when()
            )
        }
    }

    private fun setChecked(name:String): String{

    }

}