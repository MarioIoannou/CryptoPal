package com.marioioannou.cryptopal.ui.fragments.general_fragments.settings_senction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentSettingsBinding
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_AED
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_CHF
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_EUR
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_GBP
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_INR
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_JPY
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_RUB
import com.marioioannou.cryptopal.utils.Constants.CURRENCY_USD
import com.marioioannou.cryptopal.viewmodels.MainViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    lateinit var viewModel: MainViewModel
    private lateinit var currency: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        currency = viewModel.getCurrency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cvBack.setOnClickListener {
                findNavController().popBackStack()
            }
            cvCurrency.setOnClickListener {
                currencyAlertDialog()
            }

            imgBuyMeACoffee.setOnClickListener {
                val url: Uri = Uri.parse("https://www.buymeacoffee.com/marioioannou")
                val intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(intent)
            }
            //val currency = viewModel.getCurrency()
//            viewModel.readCurrency.observe(viewLifecycleOwner, Observer { value ->
//                when(value.lowercase()){
//                    "usd" -> tvCurrency.text = "USD"
//                    "eur" -> tvCurrency.text = "EUR"
//                    "gbp" -> tvCurrency.text = "GBP"
//                    "inr" -> tvCurrency.text = "INR"
//                    "chf" -> tvCurrency.text = "CHF"
//                    "jpy" -> tvCurrency.text = "JPY"
//                    "rub" -> tvCurrency.text = "RUB"
//                    "aed" -> tvCurrency.text = "AED"
//                }
//            })
            when (currency.lowercase()) {
                "usd" -> tvCurrency.text = "USD"
                "eur" -> tvCurrency.text = "EUR"
                "gbp" -> tvCurrency.text = "GBP"
                "inr" -> tvCurrency.text = "INR"
                "chf" -> tvCurrency.text = "CHF"
                "jpy" -> tvCurrency.text = "JPY"
                "rub" -> tvCurrency.text = "RUB"
                "aed" -> tvCurrency.text = "AED"
            }
        }

    }

    private fun currencyAlertDialog() {
        //var currency = DEFAULT_CURRENCY
        val currency = viewModel.getCurrency()
        var checkedItem = 0
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        ).setTitle("Theme")
        val items = arrayOf("USD", "EUR", "GBP", "INR", "CHF", "JPY", "RUB", "AED")
        when (currency.lowercase()) {
            "usd" -> checkedItem = 0
            "eur" -> checkedItem = 1
            "gbp" -> checkedItem = 2
            "inr" -> checkedItem = 3
            "chf" -> checkedItem = 4
            "jpy" -> checkedItem = 5
            "rub" -> checkedItem = 6
            "aed" -> checkedItem = 7
        }
//        viewModel.readCurrency.observe(viewLifecycleOwner, Observer { value ->
//            currency = value
//            when(currency.lowercase()){
//                "usd" -> checkedItem = 0
//                "eur" -> checkedItem = 1
//                "gbp" -> checkedItem = 2
//                "inr" -> checkedItem = 3
//                "chf" -> checkedItem = 4
//                "jpy" -> checkedItem = 5
//                "rub" -> checkedItem = 6
//                "aed" -> checkedItem = 7
//            }
//        })
        alertDialog.setSingleChoiceItems(items, checkedItem) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    checkedItem = 0
                    binding.tvCurrency.text = "USD"
                    viewModel.saveCurrency(CURRENCY_USD)
                }
                1 -> {
                    dialog.dismiss()
                    checkedItem = 1
                    binding.tvCurrency.text = "EUR"
                    viewModel.saveCurrency(CURRENCY_EUR)
                }
                2 -> {
                    dialog.dismiss()
                    checkedItem = 2
                    binding.tvCurrency.text = "GBP"
                    viewModel.saveCurrency(CURRENCY_GBP)
                }
                3 -> {
                    dialog.dismiss()
                    checkedItem = 3
                    binding.tvCurrency.text = "INR"
                    viewModel.saveCurrency(CURRENCY_INR)
                }
                4 -> {
                    dialog.dismiss()
                    checkedItem = 4
                    binding.tvCurrency.text = "CHF"
                    viewModel.saveCurrency(CURRENCY_CHF)
                }
                5 -> {
                    dialog.dismiss()
                    checkedItem = 5
                    binding.tvCurrency.text = "JPY"
                    viewModel.saveCurrency(CURRENCY_JPY)
                }
                6 -> {
                    dialog.dismiss()
                    checkedItem = 6
                    binding.tvCurrency.text = "RUB"
                    viewModel.saveCurrency(CURRENCY_RUB)
                }
                7 -> {
                    dialog.dismiss()
                    checkedItem = 7
                    binding.tvCurrency.text = "AED"
                    viewModel.saveCurrency(CURRENCY_AED)
                }
            }
        }
        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }
}