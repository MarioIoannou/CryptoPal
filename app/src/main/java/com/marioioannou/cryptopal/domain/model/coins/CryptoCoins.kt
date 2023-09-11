package com.marioioannou.cryptopal.domain.model.coins

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CryptoCoins(
    @SerializedName("coins")
    val coins: List<Coin>
) : Parcelable