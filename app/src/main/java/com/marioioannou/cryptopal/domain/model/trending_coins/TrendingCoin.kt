package com.marioioannou.cryptopal.domain.model.trending_coins


import com.google.gson.annotations.SerializedName

data class TrendingCoin(
    @SerializedName("coins")
    val coins: List<Coin>
)