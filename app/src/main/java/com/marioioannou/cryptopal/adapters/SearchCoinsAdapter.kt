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
import com.marioioannou.cryptopal.databinding.RowSearchCoinLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.Coin
import com.marioioannou.cryptopal.domain.model.search_coins.SearchedCoin

class SearchCoinsAdapter : RecyclerView.Adapter<SearchCoinsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RowSearchCoinLayoutBinding) :
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
            RowSearchCoinLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = differ.currentList[position]


        holder.binding.apply {
            imgCoin.load(coin.icon) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvTitle.text = coin.name
            Log.e("Search","${coin.name}")
            tvSymbol.text = coin.symbol?.uppercase()
            Log.e("Search","${coin.symbol?.uppercase()}")
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

}