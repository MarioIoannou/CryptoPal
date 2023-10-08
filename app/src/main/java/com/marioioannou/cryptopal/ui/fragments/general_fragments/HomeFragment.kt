package com.marioioannou.cryptopal.ui.fragments.general_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptopal.adapters.SavedCryptoCoinsAdapter
import com.marioioannou.cryptopal.adapters.TrendingCoinsAdapter
import com.marioioannou.cryptopal.databinding.FragmentHomeBinding
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.model.coins.Result
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.ui.fragments.general_fragments.settings_senction.SettingsFragmentDirections
import com.marioioannou.cryptopal.utils.NetworkListener
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.ui.viewmodels.MainViewModel
import com.marioioannou.cryptopal.utils.observeOnce
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var trendingCoinsAdapter: TrendingCoinsAdapter
    private lateinit var savedCryptoCoinsAdapter: SavedCryptoCoinsAdapter

    lateinit var viewModel: MainViewModel

    private lateinit var networkListener: NetworkListener

//    private lateinit var coins : List<CryptoCoin>
//    private lateinit var trendingCoins : List<CryptoCoin>

    private var TAG = "CoinsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lottieNoData.playAnimation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currency = viewModel.currentCurrency()

        binding.cvSettings.setOnClickListener {
            val action = SettingsFragmentDirections.actionGlobalSettingsFragment()
            findNavController().navigate(action)
        }

        binding.cvSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        viewModel.readBackOnline.observe(viewLifecycleOwner) {
            viewModel.backOnline = it
        }

        setupTrendingCoinsRecyclerView(currency)
        setupWatchlistRecyclerView(currency)
        //requestTrendingCoinsApiData()
        readDatabase()

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    viewModel.networkStatus = status
                    viewModel.showNetworkStatus()
                    if (!status) {
                        hideRecyclerView()
                    } else {
                        viewModel.readWatchlist.observe(viewLifecycleOwner, Observer { result ->
                            savedCryptoCoinsAdapter.submitList(result)
                            showRecyclerView()
                            if (viewModel.times == 1){
                                viewModel.updateSpecificCoins()
                                viewModel.times = 2
                            }

                        })
                    }
                }
        }

//        Log.d("Network status","The status is ${viewModel.networkStatus}")
//        if(viewModel.networkStatus){
//            Log.d("Network status","Inside viewModel.networkStatus should be TRUE")
//            hideRecyclerView()
//        }else{
//            Log.d("Network status","Inside viewModel.networkStatus should be TRUE")
//            showRecyclerView()
//            viewModel.showNetworkStatus()
//        }


//        viewModel.readCryptoCoin.observe(viewLifecycleOwner, Observer { result ->
//            savedCryptoCoinsAdapter.submitList(result)
//            if (result.isNullOrEmpty()){
//                hideRecyclerView()
//            }else{
//                showRecyclerView()
//            }
//        })

//        lifecycleScope.launch() {
//            networkListener = NetworkListener()
//            networkListener.checkNetworkAvailability(requireContext())
//                .collect { status ->
//                    Log.d("NetworkListener", status.toString())
//                    viewModel.networkStatus = status
//                    viewModel.showNetworkStatus()
//                    if (!status){
//                        //savedCryptoCoinsAdapter.submitList(emptyList())
//                        hideRecyclerView()
//                    }else{
//                        showRecyclerView()
//                    }
//                }
//        }

//        savedCryptoCoinsAdapter.setOnItemClickListener { coin: CryptoCoinEntity ->
//            val action =
//                HomeFragmentDirections.actionHomeFragmentToCoinDetailFragment(coin.cryptoCoin, 0)
//            findNavController().navigate(action)
//        }

        trendingCoinsAdapter.setOnItemClickListener { coin: Result ->
            val action = HomeFragmentDirections.actionHomeFragmentToCoinDetailFragment(coin, 0)
            findNavController().navigate(action)
        }
    }

//    private fun requestTrendingCoinsApiData() {
//        Log.e(TAG, "requestTrendingCoinsApiData CALLED")
//        viewModel.getCoins(viewModel.applyCoinsQueries())
//        viewModel.coinResponse.observe(viewLifecycleOwner, Observer { trendingCoinResponse ->
//            Log.e(TAG, "viewModel.trendingCoinResponse.observe")
//            when (trendingCoinResponse) {
//                is ScreenState.Loading -> {
//                    showTrendingEffect()
//                    Log.e(TAG, "   requestTrendingCoinsApiData() Response Loading")
//                }
//                is ScreenState.Success -> {
//                    Log.e(TAG, "   requestTrendingCoinsApiData() Response Success")
//
//                    trendingCoinResponse.data?.let { trending ->
//                        trendingCoinsAdapter.differ.submitList(trending.result)
//                    }
//                }
//                is ScreenState.Error -> {
//                    Log.e(TAG, "   requestTrendingCoinsApiData() Response Error")
//                    hideTrendingEffect()
//                    Toast.makeText(
//                        requireContext(),
//                        trendingCoinResponse.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        })
//    }

    private fun setupTrendingCoinsRecyclerView(currency: String) {
        trendingCoinsAdapter = TrendingCoinsAdapter(currency)
        binding.rvTrending.apply {
            adapter = trendingCoinsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupWatchlistRecyclerView(currency: String) {
        savedCryptoCoinsAdapter = SavedCryptoCoinsAdapter(currency)
        binding.rvWatchlist.apply {
            adapter = savedCryptoCoinsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun showRecyclerView() {
        binding.rvWatchlist.visibility = View.VISIBLE
        binding.noDataLayout.visibility = View.GONE
    }

    private fun hideRecyclerView() {
        binding.rvWatchlist.visibility = View.GONE
        binding.noDataLayout.visibility = View.VISIBLE
    }

    private fun showTrendingList() {
        binding.rvTrending.visibility = View.VISIBLE
        binding.lottieTrending.visibility = View.GONE
    }

    private fun hideTrendingList() {
        binding.rvTrending.visibility = View.INVISIBLE
        binding.lottieTrending.visibility = View.VISIBLE
    }

    private fun topMovingCoins(list: List<Result>): List<Result> {
        val topMovers = list.sortedBy { it.priceChange1d }
        Log.e(TAG, "Top Movers -> $topMovers")
        return topMovers
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readCryptoCoin.observeOnce(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    showTrendingList()
                    Log.e(TAG, "readDatabase() CALLED")
                    trendingCoinsAdapter.differ.submitList(database.last().cryptoCoin.result)
                } else {
                    hideTrendingList()
                    Toast.makeText(
                        requireContext(),
                        "Database is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    viewModel.networkStatus = status
                    viewModel.showNetworkStatus()

                }
        }
    }

}