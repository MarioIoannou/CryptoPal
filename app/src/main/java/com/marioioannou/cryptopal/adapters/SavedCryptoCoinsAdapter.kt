package com.marioioannou.cryptopal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowWatchlistCoinLayoutBinding
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.database.crypto_watchlist.CryptoWatchlistEntity

class SavedCryptoCoinsAdapter(
    currency: String,
) : RecyclerView.Adapter<SavedCryptoCoinsAdapter.MyViewHolder>() {

    private var coins: List<CryptoWatchlistEntity> = emptyList()

    private val currencyAdapter = currencySymbol(currency)

    fun submitList(coins: List<CryptoWatchlistEntity>) {
        this.coins = coins
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: RowWatchlistCoinLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowWatchlistCoinLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = coins[position]

        holder.binding.apply {
//            imgCoin.load(coin.cryptoCoin.icon) {
//                crossfade(600)
//                error(R.drawable.ic_image_placeholder)
//            }
//            tvTitle.text = coin.cryptoCoin.name.toString()
//            tvSymbol.text = coin.cryptoCoin.symbol.toString()
//            tvPrice.text = formatNumber(coin.cryptoCoin.price)
            tvCurrencySymbol.text = currencyAdapter
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }


    override fun getItemCount(): Int {
        return coins.size
    }

    private var onItemClickListener: ((CryptoWatchlistEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoWatchlistEntity) -> Unit) {
        onItemClickListener = listener
    }

    private fun currencySymbol(currency: String): String {
        when (currency) {
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
