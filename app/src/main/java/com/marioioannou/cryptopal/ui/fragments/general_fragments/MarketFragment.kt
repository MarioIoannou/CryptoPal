package com.marioioannou.cryptopal.ui.fragments.general_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.adapters.CryptoCoinsAdapter
import com.marioioannou.cryptopal.databinding.FragmentMarketBinding
import com.marioioannou.cryptopal.domain.model.coins.Coin
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.ui.fragments.general_fragments.settings_senction.SettingsFragmentDirections
import com.marioioannou.cryptopal.utils.Constants
import com.marioioannou.cryptopal.utils.NetworkListener
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.viewmodels.MainViewModel

class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding

    private lateinit var coinsAdapter: CryptoCoinsAdapter

    lateinit var viewModel: MainViewModel


    private lateinit var networkListener: NetworkListener

    private var currency = Constants.DEFAULT_CURRENCY

    private var TAG = "CoinsFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.times = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMarketBinding.inflate(inflater, container, false)
        binding.lottieSearch.playAnimation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvSettings.setOnClickListener {
            val action = SettingsFragmentDirections.actionGlobalSettingsFragment()
            findNavController().navigate(action)
        }

        binding.lottieSearch.setOnClickListener {
            val action = MarketFragmentDirections.actionMarketFragmentToSearchFragment()
            findNavController().navigate(action)
        }

//        viewModel.readCurrency.observe(viewLifecycleOwner, Observer { value ->
//            Log.e(TAG, "value -> $value")
//            currency = value
//        })
        //currency = viewModel.readCurrency.toString()

        Log.e(TAG, "currency -> ${viewModel.currentCurrency()}")
        setupCoinsRecyclerView(viewModel.currentCurrency())
        requestCoinsApiData()

        coinsAdapter.setOnItemClickListener { coin: Coin ->
            val action = MarketFragmentDirections.actionMarketFragmentToCoinDetailFragment(coin, 0)
            findNavController().navigate(action)
        }

//        coinsAdapter.setOnItemClickListener { coin: CryptoCoin ->
//            val action = MarketFragmentDirections.actionMarketFragmentToCoinDetailFragment(coin)
//            findNavController().navigate(action)
//        }

        binding.cvFilter.setOnClickListener {
            findNavController().navigate(R.id.action_marketFragment_to_coinFilterBottomSheet)
        }
    }

    private fun requestCoinsApiData() {
        Log.e(TAG, "requestCoinsApiData CALLED")
        viewModel.getCoins(viewModel.applyCoinsQueries())
        viewModel.coinResponse.observe(viewLifecycleOwner, Observer { coinResponse ->
            Log.e(TAG, "viewModel.coinResponse.observe")
            when (coinResponse) {
                is ScreenState.Loading -> {
                    //showShimmerEffect()
                    Log.e(TAG, "   requestCoinsApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.e(TAG, "   requestCoinsApiData() Response Success")
                    //binding.noInternetLayout.visibility = View.GONE
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        hideShimmerEffect()
//                    },1000L)
                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
                    coinResponse.data?.let { coins ->
                        coinsAdapter.differ.submitList(coins.coins)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestCoinsApiData() Response Error")
                    //hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        coinResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupCoinsRecyclerView(currency : String) {
        coinsAdapter = CryptoCoinsAdapter(currency)
        binding.rvCoins.apply {
            adapter = coinsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun currencySymbol(currency: String):String{
        when(currency){
            "EUR" -> return "€ "
            "USD" -> return "$ "
            "GBP" -> return "£ "
            "INR" -> return "₹ "
            "CHF" -> return "CHF "
            "JPY" -> return "¥ "
            "RUB" -> return "₽ "
            "AED" -> return " د.إ"
        }
        return "€"
    }

    private fun applyQueries(currency: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["vs_currency"] = currency
        queries["per_page"] = "100"
        queries["page"] = "1"
        queries["price_change_percentage"] = "1h,24h,7d,14d,30d,200d,1y"
        queries["sparkline"] = "true"

        return queries
    }
}