package com.marioioannou.cryptopal.domain.model.trending_coins


import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("item")
    val item: Item
)