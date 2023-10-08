package com.marioioannou.cryptopal.domain.model.coins


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OldCoin(
    @SerializedName("availableSupply")
    val availableSupply: Double?,
    @SerializedName("contractAddress")
    val contractAddress: String?,
    @SerializedName("decimals")
    val decimals: Int?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val coin_id: String?,
    @SerializedName("marketCap")
    val marketCap: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("priceBtc")
    val priceBtc: Double?,
    @SerializedName("priceChange1d")
    val priceChange1d: Double?,
    @SerializedName("priceChange1h")
    val priceChange1h: Double?,
    @SerializedName("priceChange1w")
    val priceChange1w: Double?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("redditUrl")
    val redditUrl: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("totalSupply")
    val totalSupply: Double?,
    @SerializedName("twitterUrl")
    val twitterUrl: String?,
    @SerializedName("volume")
    val volume: Double?,
    @SerializedName("websiteUrl")
    val websiteUrl: String?,
    var isWatchListed : Boolean = false
) : Parcelable