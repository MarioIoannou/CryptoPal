package com.marioioannou.cryptopal.domain.model.market_chart


import com.google.gson.annotations.SerializedName

data class CoinMarketChart(
    @SerializedName("market_caps")
    val marketCaps: List<List<Double>>,
    @SerializedName("prices")
    val prices: List<List<Double>>,
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double>>
)