package com.marioioannou.cryptopal.domain.model.search_coins


import com.google.gson.annotations.SerializedName
import com.marioioannou.cryptopal.domain.model.coins.Result

data class SearchCoin(
    @SerializedName("coins")
        val coins: List<Result>
)