package com.marioioannou.cryptopal.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowCoinLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin

class CryptoCoinsAdapter(
    currency:String
): RecyclerView.Adapter<CryptoCoinsAdapter.MyViewHolder>() {

    var coinCurrency = currency


    inner class MyViewHolder(val binding: RowCoinLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CryptoCoin>() {

        override fun areItemsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CryptoCoin, newItem: CryptoCoin): Boolean {
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
            val priceChange = coin.priceChange24h.toString().take(8)
            val priceChangePercentage = coin.priceChangePercentage1hInCurrency.toString().take(7)
            imgCoin.load(coin.image) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvTitle.text = coin.name
            tvSymbol.text = coin.symbol?.uppercase()
            tvCurrencySymbol.text = fiat
            tvPrice.text = coin.currentPrice.toString()
            val hourChangePrice = coin.currentPrice?.let { coin.priceChangePercentage1hInCurrency?.let { it1 ->
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

    private var onItemClickListener: ((CryptoCoin) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoCoin) -> Unit) {
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

}