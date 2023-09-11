package com.marioioannou.cryptopal.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowWatchlistCoinLayoutBinding
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import com.marioioannou.cryptopal.utils.ScreenState
import com.marioioannou.cryptopal.viewmodels.MainViewModel

class SavedCryptoCoinsAdapter(
    currency: String,
) : RecyclerView.Adapter<SavedCryptoCoinsAdapter.MyViewHolder>() {

    private var coins: List<CryptoCoinEntity> = emptyList()

    private val currencyAdapter = currencySymbol(currency)

    fun submitList(coins: List<CryptoCoinEntity>) {
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
            imgCoin.load(coin.cryptoCoin.icon) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvTitle.text = coin.cryptoCoin.name.toString()
            tvSymbol.text = coin.cryptoCoin.symbol.toString()
            tvPrice.text = coin.cryptoCoin.price.toString().take(10)
            tvCurrencySymbol.text = currencyAdapter
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }


    override fun getItemCount(): Int {
        return coins.size
    }

    private var onItemClickListener: ((CryptoCoinEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoCoinEntity) -> Unit) {
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

}
