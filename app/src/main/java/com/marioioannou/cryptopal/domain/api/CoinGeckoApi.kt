package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

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
}