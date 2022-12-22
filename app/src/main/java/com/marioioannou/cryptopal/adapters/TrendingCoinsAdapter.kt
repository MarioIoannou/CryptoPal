package com.marioioannou.cryptopal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.RowTrendingCoinLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.trending_coins.Coin
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class TrendingCoinsAdapter :
    RecyclerView.Adapter<TrendingCoinsAdapter.MyViewHolder>() {

    private var sortedCoins = emptyList<CryptoCoin>()

    inner class MyViewHolder(val binding: RowTrendingCoinLayoutBinding) :
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowTrendingCoinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val coin = differ.currentList[position]
       sortedCoins = differ.currentList.sortedByDescending { it.priceChange24h }.take(8)
       val coin = sortedCoins[position]
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
            tvPriceChange.text = coin.priceChange24h.toString().take(6)+"%"
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(coin) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.take(8).size
    }

    private var onItemClickListener: ((CryptoCoin) -> Unit)? = null
    fun setOnItemClickListener(listener: (CryptoCoin) -> Unit) {
        onItemClickListener = listener
    }
}
