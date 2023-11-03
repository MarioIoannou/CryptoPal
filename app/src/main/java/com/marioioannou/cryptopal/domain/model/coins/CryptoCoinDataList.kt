package com.marioioannou.cryptopal.domain.model.coins


import com.google.gson.annotations.SerializedName

data class CryptoCoinDataList(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("result")
    val result: List<Result>
)