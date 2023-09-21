package com.marioioannou.cryptopal.ui.fragments.general_fragments.news

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.marioioannou.cryptopal.adapters.NewsAdapter
import com.marioioannou.cryptopal.databinding.FragmentNewsBinding
import com.marioioannou.cryptopal.domain.model.news.New
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.ui.fragments.general_fragments.settings_senction.SettingsFragmentDirections
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import java.util.*
import java.util.logging.Handler
import kotlin.collections.HashMap

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    lateinit var viewModel: MainViewModel

    private lateinit var newsAdapter: NewsAdapter

    private var TAG = "NewsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieNews.visibility = View.GONE
            binding.rvNews.visibility = View.VISIBLE
        }, 2000L)

        binding.cvSettings.setOnClickListener {
            val action = SettingsFragmentDirections.actionGlobalSettingsFragment()
            findNavController().navigate(action)
        }

        setupNewsRecyclerView()
        requestNewsApiData("trending")

        newsAdapter.setOnItemClickListener { news: New ->
            val action = NewsFragmentDirections.actionNewsFragmentToArticleFragment(news)
            findNavController().navigate(action)
        }

        binding.chipGroupFilter.setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedTime = chip.text.toString().lowercase(Locale.ROOT)
            chip.isChecked = true
            group.requestChildFocus(chip,chip)
            requestNewsApiData(selectedTime)
        }
    }

    private fun requestNewsApiData(filter: String) {
        Log.e(TAG, "requestCoinsApiData CALLED")
        viewModel.getNews(filter,applyQueries())
        viewModel.newsResponse.observe(viewLifecycleOwner, Observer { newsResponse ->
            Log.e(TAG, "viewModel.coinResponse.observe")
            when (newsResponse) {
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
                    newsResponse.data?.let { news ->
                        newsAdapter.differ.submitList(news.news)
                    }
                }
                is ScreenState.Error -> {
                    Log.e(TAG, "   requestCoinsApiData() Response Error")
                    //hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        newsResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["skip"] = "0"
        queries["limit"] = "20"
        return queries
    }

    private fun setupNewsRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}