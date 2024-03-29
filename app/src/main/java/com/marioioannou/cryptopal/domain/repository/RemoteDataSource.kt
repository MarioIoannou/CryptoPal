package com.marioioannou.cryptopal.domain.repository

import android.util.Log
import com.marioioannou.cryptopal.domain.api.CoinGeckoApi
import com.marioioannou.cryptopal.domain.api.CoinStatsApi
import com.marioioannou.cryptopal.domain.database.CryptoCoinDAO
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.domain.model.coins.Result
import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
//    @Named("CoinStats") private val coinStatsApi: CoinStatsApi,
//    @Named("CoinGecko") private val coinGeckoApi: CoinGeckoApi
    private val coinStatsApi: CoinStatsApi,
    private val coinGeckoApi: CoinGeckoApi,
    private val cryptoCoinDAO: CryptoCoinDAO,
) {

    suspend fun getCoins(queries: Map<String, String>): Response<CryptoCoins> {
        return coinStatsApi.getCryptoCoins(queries)
    }

    suspend fun getCoinInfo(coin_id: String, currency: String): Response<Result> {
        Log.e("RemoteDataSource", "getCoins")
        return coinStatsApi.getCryptoCoin(coin_id,currency)
    }

    suspend fun getCryptoCoinMarketChart(
        id: String,
        currency: String,
        days: String,
    ): Response<CoinMarketChart> {
        //return CoinApiService.coinApi.getCryptoCoinMarketChart(id, currency, days)
        return coinGeckoApi.getCryptoCoinMarketChart(id, currency, days)
    }

    suspend fun getSearchedCoin(query: String): Response<SearchCoin> {
        //return CoinApiService.coinApi.getSearchedCoin(query)
        return coinGeckoApi.getSearchedCoin(query)
    }

    suspend fun getNews(filter: String, queries: Map<String, String>): Response<CryptoNews> {
        return coinStatsApi.getNews(filter, queries)
    }
}