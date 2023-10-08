package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import retrofit2.Response
import retrofit2.http.*

interface CoinStatsApi {

//    @GET("/api/v3/coins/markets")
//    suspend fun getCryptoCoins(
//        @QueryMap queries: Map<String,String>
//    ): Response<List<CryptoCoin>>

    @GET("/coins")
    suspend fun getCryptoCoins(
        @QueryMap queries: Map<String,String>
    ): Response<CryptoCoins>

    @GET("/coins/{coin_id}")
    suspend fun getCryptoCoin(
        @Path("coin_id") coinId: String,
        @Query("currency") currency: String
    ): Response<CoinInfo>

    @GET("/public/v1/news/{filter}")
    suspend fun getNews(
        @Path("filter") filter: String,
        @QueryMap queries: Map<String,String>
    ): Response<CryptoNews>
}