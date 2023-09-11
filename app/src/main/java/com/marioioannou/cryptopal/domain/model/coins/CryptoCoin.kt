package com.marioioannou.cryptopal.domain.model.coins


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class XXCryptoCoin(
    @SerializedName("ath")
    val ath: Double?,
    @SerializedName("ath_change_percentage")
    val athChangePercentage: Double?,
    @SerializedName("ath_date")
    val athDate: String?,
    @SerializedName("atl")
    val atl: Double?,
    @SerializedName("atl_change_percentage")
    val atlChangePercentage: Double?,
    @SerializedName("atl_date")
    val atlDate: String?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?,
    @SerializedName("current_price")
    val currentPrice: Double?,
    @SerializedName("fully_diluted_valuation")
    val fullyDilutedValuation: Long?,
    @SerializedName("high_24h")
    val high24h: Double?,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("low_24h")
    val low24h: Double?,
    @SerializedName("market_cap")
    val marketCap: Long?,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24h: Double?,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double?,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,
    @SerializedName("max_supply")
    val maxSupply: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price_change_24h")
    val priceChange24h: Double?,
    @SerializedName("price_change_percentage_14d_in_currency")
    val priceChangePercentage14dInCurrency: Double?,
    @SerializedName("price_change_percentage_1h_in_currency")
    val priceChangePercentage1hInCurrency: Double?,
    @SerializedName("price_change_percentage_1y_in_currency")
    val priceChangePercentage1yInCurrency: Double?,
    @SerializedName("price_change_percentage_200d_in_currency")
    val priceChangePercentage200dInCurrency: Double?,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,
    @SerializedName("price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: Double?,
    @SerializedName("price_change_percentage_30d_in_currency")
    val priceChangePercentage30dInCurrency: Double?,
    @SerializedName("price_change_percentage_7d_in_currency")
    val priceChangePercentage7dInCurrency: Double?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("total_volume")
    val totalVolume: Double?,
    @SerializedName("sparkline_in_7d")
    val sparklineIn7d: SparklineIn7d?,
    @SerializedName("large")
    val large: String?,
    var isWatchListed : Boolean = false
) : Parcelable