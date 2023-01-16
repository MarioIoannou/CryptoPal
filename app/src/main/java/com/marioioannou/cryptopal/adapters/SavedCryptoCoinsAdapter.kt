package com.marioioannou.cryptopal.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowTrendingCoinLayoutBinding
import com.marioioannou.cryptopal.databinding.RowWatchlistCoinLayoutBinding
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.trending_coins.Coin
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SavedCryptoCoinsAdapter(
    currency:String
) :
    RecyclerView.Adapter<SavedCryptoCoinsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RowWatchlistCoinLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CryptoCoinEntity>() {

        override fun areItemsTheSame(
            oldItem: CryptoCoinEntity,
            newItem: CryptoCoinEntity,
        ): Boolean {
            return oldItem.cryptoCoin.name == newItem.cryptoCoin.name
        }

        override fun areContentsTheSame(
            oldItem: CryptoCoinEntity,
            newItem: CryptoCoinEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowWatchlistCoinLayoutBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = differ.currentList[position]
        val coin = list.cryptoCoin
        val dataSetLine = ArrayList<Int>()
        holder.binding.apply {
            //val priceChange = coin.priceChangePercentage24h.toString()
            imgCoin.load(coin.image) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            //imgFood.background(recipe.image.)
            tvTitle.text = coin.name
            tvSymbol.text = coin.symbol?.uppercase()
            tvPrice.text = coin.currentPrice.toString().take(8)
            watchlistChart.lineColor = ContextCompat.getColor(holder.itemView.context, R.color.my_blue)
            val data = coin.sparklineIn7d?.price
            Log.e("Adapter","$data")
            if (data != null) {
//                for (i in data) {
//                    dataSetLine.add(i.toInt())
//                }
                //watchlistChart.setData(dataSetLine)
                val adapter = SparkLineAdapter(data)
                watchlistChart.adapter = adapter
            }else{
                watchlistChart.visibility=View.GONE
                noData.visibility=View.VISIBLE
            }

//            val data = coin.sparklineIn7d?.price
//            for (i in 0 .. data?.size!!) {
//                dataSetLine.add(Entry(data[i].toFloat(), data[i + 1].toFloat()))
//            }
//            with(watchlistChart) {
//                axisLeft.isEnabled = false
//                axisRight.isEnabled = false
//                xAxis.isEnabled = false
//                legend.isEnabled = false
//                description.isEnabled = false
//            }
//            val dataSet = LineDataSet(dataSetLine, "Unused label").apply {
//                color = ContextCompat.getColor(holder.itemView.context, R.color.my_blue)
//                valueTextColor = ContextCompat.getColor(holder.itemView.context, R.color.my_dark_grey)
//                highLightColor = ContextCompat.getColor(holder.itemView.context, R.color.yellow)
//                setDrawValues(false)
//                lineWidth = 1.5f
//                isHighlightEnabled = true
//                setDrawHighlightIndicators(false)
//            }
//            watchlistChart.data = LineData(dataSet)
//            watchlistChart.invalidate()
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(list) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.take(8).size
    }

    private var onItemClickListener: ((CryptoCoinEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoCoinEntity) -> Unit) {
        onItemClickListener = listener
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
}
