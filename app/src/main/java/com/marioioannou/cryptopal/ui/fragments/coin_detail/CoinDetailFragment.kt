package com.marioioannou.cryptopal.ui.fragments.coin_detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.chip.Chip
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.FragmentCoinDetailBinding
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.model.coins.Coin
import com.marioioannou.cryptopal.ui.activities.MainActivity
import com.marioioannou.cryptopal.ui.fragments.SettingsFragmentDirections
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CoinDetailFragment : Fragment() {

    private lateinit var binding: FragmentCoinDetailBinding

    private val args: CoinDetailFragmentArgs by navArgs()

    lateinit var viewModel: MainViewModel

    //private var savedCoin = false
    private var savedCoinId = 0

    //private var chartData = ArrayList<Entry>()

    private var TAG = "CoinDetailFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCoinDetailBinding.inflate(inflater, container, false)

        requestChartData(args.coin.coin_id.toString(), "usd", "24h")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSavedRecipes()

        val coin = args.coin
        binding.tvCurrency.text = currencySymbol(viewModel.getCurrency())
        binding.detailTvCurrency.text = currencySymbol(viewModel.getCurrency())
        Log.e(TAG, " tvCurrency ${binding.tvCurrency.text}")

        Log.e(TAG, "is ${coin.name} saved? ${viewModel.savedCoin}")

        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnInfoAbout.setOnClickListener {
            val action = CoinDetailFragmentDirections.actionCoinDetailFragmentToInfoFragment()
            findNavController().navigate(action)
        }

        binding.cvSave.setOnClickListener {
            if (!viewModel.savedCoin) {
                saveToFavorites(args.coin)
            } else {
                removeFromFavorites()
            }
        }

        binding.lottieChart.playAnimation()
        binding.lottieChart.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieChart.visibility = View.GONE
            binding.chartLine.visibility = View.VISIBLE
            binding.lottieChart.pauseAnimation()
        }, 500L)

        //val coin = args

//        Log.e(TAG, "requestCoinInfoApiData CALLED")
//        Log.e(TAG, "coin.coinId is ${coin.coinId}")
//        viewModel.getCryptoCoinInfo(coin.coinId,applyInfoQueries())
//        viewModel.coinInfoResponse.observe(viewLifecycleOwner, Observer { coinResponse ->
//            Log.e(TAG, "viewModel.coinInfoResponse.observe")
//            when (coinResponse) {
//                is ScreenState.Loading -> {
//                    //showShimmerEffect()
//                    Log.e(TAG, "   requestCoinInfoApiData() Response Loading")
//                }
//                is ScreenState.Success -> {
//                    Log.e(TAG, "   requestCoinInfoApiData() Response Success")
//                    //binding.noInternetLayout.visibility = View.GONE
////                    Handler(Looper.getMainLooper()).postDelayed({
////                        hideShimmerEffect()
////                    },1000L)
//                    //binding.rvCoinRecyclerview.visibility = View.VISIBLE
//                    coinResponse.data?.let { coins ->
//                        binding.apply {
//                            //detailImgCoin.load(coin.image)
//                            detailTvTitle.text = coins.name
//                            detailTvSymbol.text = coins.symbol.uppercase()
//                            detailTvPrice.text = coins.marketData.currentPrice.usd.toString()
//                            val price = coins.marketData.priceChange24h.toString().take(6)
//                            val pricePercentage = coins.marketData.priceChangePercentage24hInCurrency.usd.toString().take(6)
//                            tvChangePrice.text = price
//                            tvChangePercentage.text = "$pricePercentage%"
//                        }
//                    }
//                }
//                is ScreenState.Error -> {
//                    Log.e(TAG, "   requestCoinInfoApiData() Response Error")
//                    //hideShimmerEffect()
//                    Toast.makeText(
//                        requireContext(),
//                        coinResponse.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        })


        if (args.fromSearch == 1) {
            viewModel.getCoinInfo(args.coin.coin_id.toString(),"usd")
            viewModel.coinResponse.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
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
                        response.data?.let { coin ->
                            binding.apply {
                                detailTvPrice.text = coin.coins[0].price.toString()
                                val price = coin.coins[0].price.toString().take(10)
                                val pricePercentage =
                                    coin.coins[0].priceChange1d.toString().take(10)
                                tvChangePrice.text = price
                                tvChangePercentage.text = "$pricePercentage%"
                                //infoHigh24h.text = coin.coins[0].high24h.toString().take(10)
                                //infoLow24h.text = coin.coins[0].low24h.toString().take(10)
                                //infoAth.text = coin.coins[0].ath.toString().take(10)
                                //infoAtl.text = coin.coins[0].atl.toString().take(10)
                                infoMarketCap.text = coin.coins[0].marketCap.toString()
//                                infoCirculatingSupply.text =
//                                    coin.coins[0].circulatingSupply.toString().take(10)
                                infoMaxSupply.text = coin.coins[0].totalSupply.toString().take(10)
                                infoPriceIn1w.text =
                                    coin.coins[0].priceChange1w.toString().take(10)
                                infoMarketCapRank.text = "#" + coin.coins[0].rank.toString()
                                imgCryptoLogo.load(coin.coins[0].icon) {
                                    crossfade(600)
                                    error(R.drawable.ic_image_placeholder)
                                }
                            }
                        }
                    }
                    is ScreenState.Error -> {
                        Log.e(TAG, "   requestCoinsApiData() Response Error")
                        //hideShimmerEffect()
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        binding.apply {
            //detailImgCoin.load(coin.image)
            detailTvTitle.text = coin.name
            detailTvSymbol.text = coin.symbol?.uppercase()
            detailTvPrice.text = coin.price.toString()
            val price = coin.price.toString().take(10)
            val pricePercentage = coin.priceChange1d.toString().take(10)
            tvChangePrice.text = price
            tvChangePercentage.text = "$pricePercentage%"
            infoCoinName.text = coin.name
            //infoHigh24h.text = coin.high24h.toString().take(10)
            //infoLow24h.text = coin.low24h.toString().take(10)
            //infoAth.text = coin.ath.toString().take(10)
            //infoAtl.text = coin.atl.toString().take(10)
            infoMarketCap.text = coin.marketCap.toString()
            //infoCirculatingSupply.text = coin.circulatingSupply.toString().take(10)
            infoMaxSupply.text = coin.totalSupply.toString().take(10)
            infoPriceIn1w.text = coin.priceChange1w.toString().take(10)
            infoMarketCapRank.text = "#" + coin.rank.toString()
            imgCryptoLogo.load(coin.icon) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
        }

        //Log.e(TAG, "IN ON CREATE dataSet: $chartData")

        binding.segmentedButtonGroup.setOnPositionChangedListener { position ->
            when (position) {
                0 -> {
                    binding.chartCandle.visibility = View.GONE
                    binding.chartLine.visibility = View.VISIBLE
                    //lineChart(chartData)
                }

                1 -> {
                    binding.chartLine.visibility = View.GONE
                    binding.chartCandle.visibility = View.VISIBLE
                    //stickChart()
                }
            }
        }

        binding.chipGroupTime.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedTime = chip.text.toString().lowercase(Locale.ROOT)
            chip.isChecked = true
            group.requestChildFocus(chip, chip)
            viewModel.getCryptoCoinMarketChart(coin.coin_id.toString(), "usd", selectedTime)
        }
    }

    private fun requestChartData(coinId: String, currency: String, days: String) {
        viewModel.getCryptoCoinMarketChart(coinId, currency, days)
        viewModel.coinMarketChartResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is ScreenState.Loading -> {

                }

                is ScreenState.Success -> {
                    val dataSetLine = ArrayList<Entry>()
                    val dataSet = ArrayList<Double>()
                    val dataSetCandle: MutableList<CandleEntry> = mutableListOf()
                    val dataSetResponse = response.data?.prices
                    val dataSetCandleResponse = response.data
                    for (i in 0 until dataSetCandleResponse!!.prices.size - 1) {
                        dataSet.add(response.data.prices[i][1])
                    }
                    dataSetResponse?.map { value ->
                        dataSetLine.add(Entry(value[0].toFloat(), value[1].toFloat()))
                    }
                    Log.e(TAG, "IN RESPONSE Sublist: ${response.data?.prices?.subList(0, 3)}")
                    /*dataSetCandleResponse?.prices?.map { item ->

                    }*/
                    for (i in 0 until dataSetCandleResponse!!.prices.size - 1) {
                        val listOne = response.data.prices[i][1]
                        //Log.e(TAG, "List 1: $listOne")
                        val listTwo = response.data.prices[i + 1][1]
                        //Log.e(TAG, "List 2: $listTwo")
                        //dataSetCandle.add(CandleEntry(i.toFloat(),listOne[1].toFloat(),listTwo[1].toFloat(),listOne[0].toFloat(),listTwo[0].toFloat()))
                        //dataSetLine.add(Entry(listTwo.toFloat(), listOne.toFloat()))
                        dataSetCandle.add(CandleEntry(i.toFloat(),
                            listOne.toFloat(),
                            listTwo.toFloat(),
                            listOne.toFloat(),
                            listTwo.toFloat()))
                    }
//                    dataSetResponse?.map { value ->
//                        dataSetCandle.add(CandleEntry(i.toFloat(),
//                            value[i.toInt()].toFloat(),
//                            value[i.toInt() + 1].toFloat(),
//                            value[j.toInt()].toFloat(),
//                            value[j.toInt() + 1].toFloat()))
//
//                    }
                    lineChart(dataSetLine)
                    candleChart(dataSetCandle)
                    Log.e(TAG,
                        "IN RESPONSE Line dataSet: $dataSetLine")
                    Log.e(TAG, "IN RESPONSE Candle dataSet: $dataSetCandle")
                    Log.e(TAG, "IN RESPONSE dataSet: $dataSet")
                }

                is ScreenState.Error -> {
                }
            }
        })
    }

    private fun lineChart(chartData: ArrayList<Entry>) {
        val chartItems = LineDataSet(chartData, "${args.coin.name} data")
        chartItems.apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = ContextCompat.getColor(requireContext(), R.color.my_blue)
            highLightColor = ContextCompat.getColor(requireContext(), R.color.my_blue)
            setDrawValues(false)
            setDrawFilled(true)
            setDrawCircles(false)
            lineWidth = 1.5f
            //fillColor = ContextCompat.getColor(requireContext(),R.drawable.chart_background)
            setDrawCircleHole(false)
            fillDrawable =
                AppCompatResources.getDrawable(requireContext(), R.drawable.chart_background)
        }
        binding.chartLine.apply {
            data = LineData(chartItems)
//            description.isEnabled = false
//            isDragEnabled = false
//            xAxis.isEnabled = false
//            axisLeft.setDrawAxisLine(false)
//            axisLeft.textColor = Color.BLUE
//            axisRight.isEnabled = false
//            legend.isEnabled = false
//            setTouchEnabled(false)
//            setScaleEnabled(false)
//            setDrawGridBackground(false)
//            setDrawBorders(false)
//            animateX(1000)
//            invalidate()
            description.isEnabled = false
            isDragEnabled = false
            xAxis.isEnabled = false
            axisLeft.setDrawAxisLine(false)
            axisLeft.textColor = Color.BLUE
            axisRight.isEnabled = false
            legend.isEnabled = false
            xAxis.valueFormatter
            setTouchEnabled(false)
            setScaleEnabled(false)
            setDrawGridBackground(false)
            setDrawBorders(false)
            animateX(1000)
            invalidate()
        }
    }

    private fun candleChart(chartData: MutableList<CandleEntry>) {
        val chartItems = CandleDataSet(chartData, "${args.coin.name} data")
        chartItems.apply {
            color = resources.getColor(R.color.my_blue)
            shadowColor = resources.getColor(R.color.my_blue_2)
            shadowWidth = 1f
            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL
        }
        binding.chartCandle.apply {
            data = CandleData(chartItems)
            setBackgroundColor(resources.getColor(R.color.white))
            animateXY(3000, 3000)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
        }
    }

//    private fun applyQueries(id: String): HashMap<String, String> {
//        val queries: HashMap<String, String> = HashMap()
//        queries["vs_currency"] = "usd"
//        queries["ids"] = id
//        queries["price_change_percentage"] = "1h,24h,7d,14d,30d,200d,1y"
//        queries["sparkline"] = "true"
//
//        return queries
//    }

    private fun percentage(price: Double?, percent: Double?): Double {
        return (percent!! / 100) * price!!
    }

    private fun checkSavedRecipes() {
        viewModel.readCryptoCoin.observe(viewLifecycleOwner) { cryptoCoinEntity ->
            try {
                for (coin in cryptoCoinEntity) {
                    if (coin.cryptoCoin.coin_id == args.coin.coin_id) {
                        binding.imgFavorite.setTint(R.color.yellow)
                        savedCoinId = coin.id
                        viewModel.savedCoin = true
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }

    private fun saveToFavorites(selectedCoin: Coin) {
        val cryptoCoinEntity =
            CryptoCoinEntity(
                0,
                selectedCoin
            )
        viewModel.insertCryptoCoin(cryptoCoinEntity)
        cryptoCoinEntity.cryptoCoin.isWatchListed = true
        binding.imgFavorite.setTint(R.color.yellow)
        viewModel.savedCoin = true
    }

    private fun removeFromFavorites() {
        val cryptoCoinEntity =
            CryptoCoinEntity(
                savedCoinId,
                args.coin
            )
        viewModel.deleteCryptoCoin(cryptoCoinEntity)
        binding.imgFavorite.setTint(R.color.white)
        viewModel.savedCoin = false
    }

    private fun ImageView.setTint(@ColorRes color: Int?) {
        if (color == null) {
            ImageViewCompat.setImageTintList(this, null)
        } else {
            ImageViewCompat.setImageTintList(this,
                ColorStateList.valueOf(ContextCompat.getColor(context, color)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.imgFavorite.setTint(R.color.white)
    }

    private fun currencySymbol(currency: String): String {
        return when (currency.uppercase()) {
            "EUR" -> return "€"
            "USD" -> return "$"
            "GBP" -> return "£"
            "INR" -> return "₹"
            "CHF" -> return "CHF"
            "JPY" -> return "¥"
            "RUB" -> return "₽"
            "AED" -> return "د.إ"
            else -> {
                "$"
            }
        }
    }
}