package com.marioioannou.cryptopal.domain.model.search_coins


import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("api_symbol")
    val apiSymbol: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("large")
    val large: String,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("thumb")
    val thumb: String
)