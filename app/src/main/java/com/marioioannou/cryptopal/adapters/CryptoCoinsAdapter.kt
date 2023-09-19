package com.marioioannou.cryptopal.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowCoinLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.Coin
import java.lang.Math.floor

class CryptoCoinsAdapter(
    currency:String
): RecyclerView.Adapter<CryptoCoinsAdapter.MyViewHolder>() {

    var coinCurrency = currency


    inner class MyViewHolder(val binding: RowCoinLayoutBinding) :
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

    //private var recipes = emptyList<com.marioioannou.mealquest.domain.model.recipes.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder.from(parent)
        return MyViewHolder(
            RowCoinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = differ.currentList[position]
        val fiat = currencySymbol(coinCurrency)
        //Log.e("Currency", fiat)
        holder.binding.apply {
            val priceChange = formatNumber(coin.priceChange1d)
            val priceChangePercentage = coin.priceChange1h.toString().take(5)
            imgCoin.load(coin.icon) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvTitle.text = coin.name
            tvSymbol.text = coin.symbol?.uppercase()
            tvCurrencySymbol.text = fiat
            tvPrice.text = formatNumber(coin.price)
            val hourChangePrice = coin.price?.let { coin.priceChange1h?.let { it1 ->
                percentage(it,
                    it1).toString().take(8)
            } }

            if (priceChange.contains("-") && priceChangePercentage.contains("-")) {
                tvPriceChange.text = "$fiat $hourChangePrice"
                tvPriceChange.setTextColor(Color.RED)
                //tvChangePercentage.filters = arrayOf(InputFilter.LengthFilter(4))
                tvChangePercentage.text = "$priceChangePercentage%"
                tvChangePercentage.setTextColor(Color.RED)
                cvChangePercentage.strokeColor = Color.parseColor("#ffb3b3")
            } else {
                tvPriceChange.text = "$fiat $hourChangePrice"
                tvPriceChange.setTextColor(Color.GREEN)
               // tvChangePercentage.filters = arrayOf(InputFilter.LengthFilter(4))
                tvChangePercentage.text = "$priceChangePercentage%"
                tvChangePercentage.setTextColor(Color.GREEN)
                cvChangePercentage.strokeColor = Color.parseColor("#b3ffb3")
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Coin) -> Unit)? = null
    fun setOnItemClickListener(listener: (Coin) -> Unit) {
        onItemClickListener = listener
    }

//    private fun percentage(price: Double?, percent: Double?): Double {
//        return (percent?.times(100))!! / price!!
//    }

    private fun percentage(price: Double, percent: Double): Double {
        return (percent.div(100)).times(price)
    }

    private fun currencySymbol(currency: String):String{
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