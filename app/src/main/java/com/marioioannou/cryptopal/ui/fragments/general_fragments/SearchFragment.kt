package com.marioioannou.cryptopal.ui.fragments.general_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marioioannou.cryptopal.adapters.CryptoCoinsAdapter
import com.marioioannou.cryptopal.adapters.SearchCoinsAdapter
import com.marioioannou.cryptopal.databinding.FragmentSearchBinding
import com.marioioannou.cryptopal.domain.model.coins.Coin
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.utils.NetworkListener
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.utils.hideKeyboard
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var coinsAdapter: CryptoCoinsAdapter

    lateinit var viewModel: MainViewModel

    private lateinit var searchCoinsAdapter: SearchCoinsAdapter

    private lateinit var networkListener: NetworkListener

    private var TAG = "CoinsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lottieCryptos.playAnimation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            //binding.shimmerIngredientsSearchRv.startShimmer()
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        requestSearchedApiData(editable.toString())
                    }
                }
            }
        }

        setupRecyclerView()

        searchCoinsAdapter.setOnItemClickListener {coin: Coin ->
            val action = SearchFragmentDirections.actionSearchFragmentToCoinDetailFragment(coin, 1)
            findNavController().navigate(action)  }
    }

    private fun setupRecyclerView() {
        searchCoinsAdapter = SearchCoinsAdapter()
        binding.rvSearchedCoins.apply {
            adapter = searchCoinsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun requestSearchedApiData(searchedCoin: String) {
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getSearchedCoin(searchedCoin)
        viewModel.searchCoinResponse.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (response) {
                is ScreenState.Loading -> {
//                    showShimmerEffect()
//                    binding.layoutNoResult.visibility = View.GONE
                    //Log.e(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    //Log.e(TAG, "   requestApiData() Response Success")
                    //binding.layoutNoResult.visibility = View.GONE
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        hideShimmerEffect()
//                    },100L)
                    binding.lottieCryptos.visibility = View.GONE
                    binding.rvSearchedCoins.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideKeyboard()
                    },1000L)
                    response.data?.let { results ->
                        searchCoinsAdapter.differ.submitList(results.coins)
                    }
                }
                is ScreenState.Error -> {
                    //searchError()
                    //Log.e(TAG, "   requestApiData() Response Error")
                }
            }
        })
    }
}