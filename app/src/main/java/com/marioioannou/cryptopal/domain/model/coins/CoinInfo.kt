package com.marioioannou.cryptopal.domain.model.coins

import com.google.gson.annotations.SerializedName

data class CoinInfo (
    @SerializedName("coin")
    val coin: Result
)