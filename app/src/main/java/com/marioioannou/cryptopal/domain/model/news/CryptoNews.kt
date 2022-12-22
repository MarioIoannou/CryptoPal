package com.marioioannou.cryptopal.domain.model.news


import com.google.gson.annotations.SerializedName

data class CryptoNews(
    @SerializedName("news")
    val news: List<New>
)