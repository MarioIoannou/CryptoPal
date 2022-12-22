package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import com.marioioannou.cryptopal.domain.model.trending_coins.TrendingCoin
import retrofit2.Response
import retrofit2.http.*

interface CryptoApi {

    @GET("/api/v3/coins/markets")
    suspend fun getCryptoCoins(
        @QueryMap queries: Map<String,String>
    ): Response<List<CryptoCoin>>

    @GET("/api/v3/search/trending")
    suspend fun getTrendingCoin(
    ): Response<TrendingCoin>

    @GET("/api/v3/coins/{id}/market_chart")
    suspend fun getCryptoCoinMarketChart(
        @Path("id") cryptoId: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: String
    ): Response<CoinMarketChart>

    @GET("/api/v3/search")
    suspend fun getSearchedCoin(
        @Query("query") query : String,
    ): Response<SearchCoin>

    @GET("/public/v1/news/{filter}")
    suspend fun getNews(
        @Path("filter") filter: String,
        @QueryMap queries: Map<String,String>
    ): Response<CryptoNews>
}