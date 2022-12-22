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
import com.marioioannou.cryptopal.databinding.RowNewsLayoutBinding
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.news.New

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RowNewsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<New>() {

        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //private var recipes = emptyList<com.marioioannou.mealquest.domain.model.recipes.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //return MyViewHolder.from(parent)
        return MyViewHolder(
            RowNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = differ.currentList[position]
        holder.binding.apply {
            imgNews.load(news.imgURL) {
                crossfade(600)
                error(R.drawable.ic_image_placeholder)
            }
            tvTitle.text = news.title
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(news) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((New) -> Unit)? = null
    fun setOnItemClickListener(listener: (New) -> Unit) {
        onItemClickListener = listener
    }

//    private fun percentage(price: Double?, percent: Double?): Double {
//        return (percent?.times(100))!! / price!!
//    }

}