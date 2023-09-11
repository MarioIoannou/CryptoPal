package com.marioioannou.cryptopal.domain.model.coins

import com.google.gson.annotations.SerializedName
import com.marioioannou.cryptopal.domain.model.coins.Coin

data class CoinInfo (
    @SerializedName("coin")
    val coin: Coin
)