package com.marioioannou.cryptopal.domain.model.trending_coins


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("coin_id")
    val coinId: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("large")
    val large: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("thumb")
    val thumb: String
)