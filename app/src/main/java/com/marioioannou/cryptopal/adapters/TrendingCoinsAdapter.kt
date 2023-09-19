package com.marioioannou.cryptopal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowTrendingCoinLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.Coin

class TrendingCoinsAdapter(
    currency: String,
) :
    RecyclerView.Adapter<TrendingCoinsAdapter.MyViewHolder>() {

    var coinCurrency = currency
    private var sortedCoins = emptyList<Coin>()

    inner class MyViewHolder(val binding: RowTrendingCoinLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Coin>() {

        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowTrendingCoinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val coin = differ.currentList[position]
        sortedCoins = differ.currentList.sortedByDescending { it.priceChange1d }.take(8)
        val coin = sortedCoins[position]
        val fiat = currencySymbol(coinCurrency)
        holder.binding.apply {
            //val priceChange = coin.priceChangePercentage24h.toString()
            imgCoin.load(coin.icon) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            //imgFood.background(recipe.image.)
            tvCurrencySymbol.text = fiat
            tvTitle.text = coin.name
            tvSymbol.text = coin.symbol?.uppercase()
            tvPrice.text = formatNumber(coin.price)
            tvPriceChange.text = coin.priceChange1d.toString().take(8) + "%"
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.take(8).size
    }

    private var onItemClickListener: ((Coin) -> Unit)? = null
    fun setOnItemClickListener(listener: (Coin) -> Unit) {
        onItemClickListener = listener
    }

    private fun currencySymbol(currency: String): String {
        when (currency.uppercase()) {
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

//    private fun formatNumber(num: Double?): String {
//        return when {
//            num!! % 1 == 0.0 -> num.toInt().toString()
//            kotlin.math.floor(num) != num && Math.floor(num * 10) == num * 10 -> String.format("%.7f", num)
//            else -> String.format("%.2f", num)
//        }
//    }

    private fun formatNumber(num: Double?): String {
        if (num != null) {
            return when {
                num % 1 == 0.0 -> num.toInt().toString()
                num < 1 && num > 0 -> {
                    var tempNum = num
                    var decimalPlaces = 0
                    while (tempNum < 1) {
                        tempNum *= 10
                        decimalPlaces++
                    }
                    val totalDecimalPlaces = if (decimalPlaces + 1 > 7) 7 else decimalPlaces + 1
                    String.format("%.${totalDecimalPlaces}f", num)
                }
                else -> String.format("%.2f", num)
            }
        }
        return "0"
    }
}
