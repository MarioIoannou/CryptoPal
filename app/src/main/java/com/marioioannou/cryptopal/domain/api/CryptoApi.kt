package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.trending_coins.TrendingCoin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CryptoApi {

    @GET("/coins/markets")
    suspend fun getRecipes(
        @QueryMap queries: Map<String,String>
    ): Response<List<CryptoCoin>>

    @GET("/search/trending")
    suspend fun getSearchedIngredients(
    ): Response<TrendingCoin>

}