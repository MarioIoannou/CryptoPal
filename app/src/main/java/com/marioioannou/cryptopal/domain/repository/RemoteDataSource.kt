package com.marioioannou.cryptopal.domain.repository

import android.util.Log
import com.marioioannou.cryptopal.domain.api.CoinApiService
import com.marioioannou.cryptopal.domain.api.CryptoApi
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin
import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import com.marioioannou.cryptopal.domain.model.trending_coins.TrendingCoin
import retrofit2.Response
import retrofit2.http.Url
import javax.inject.Inject
import javax.inject.Named

class RemoteDataSource @Inject constructor(
    private val cryptoApi: CryptoApi,
) {

    suspend fun getCoins(queries: Map<String, String>): Response<List<CryptoCoin>> {
        Log.e("RemoteDataSource", "getCoins")
        return cryptoApi.getCryptoCoins(queries)
    }

    suspend fun getTrendingCoins(): Response<TrendingCoin> {
        return cryptoApi.getTrendingCoin()
    }

    suspend fun getCryptoCoinMarketChart(
        id: String,
        currency: String,
        days: String,
    ): Response<CoinMarketChart> {
        return cryptoApi.getCryptoCoinMarketChart(id, currency, days)
    }

    suspend fun getSearchedCoin(query: String): Response<SearchCoin> {
        return cryptoApi.getSearchedCoin(query)
    }

    suspend fun getNews(filter: String, queries: Map<String, String>): Response<CryptoNews> {
        return CoinApiService.coinApi.getNews(filter, queries)
    }
}