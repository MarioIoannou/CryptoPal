package com.marioioannou.cryptopal.utils

import com.marioioannou.cryptopal.BuildConfig

object Constants {
    //https://api.coingecko.com/api/v3/coins/
    const val BASE_URL = "https://api.coingecko.com/api/v3/"
    const val API_KEY = BuildConfig.API_KEY

    //API QUERY - COINS
    const val QUERY_COINS_CURRENCY = "vs_currency"
    const val QUERY_COINS_NUMBER = "per_page"
    const val QUERY_COINS_PAGE = "page"
    const val QUERY_COINS_SPARKLINE = "sparkline"

    //API QUERY - TRENDING COINS

    //API QUERY - SEARCHING COINS

    //API QUERY - NEWS

    //DATASTORE
    const val DEFAULT_CURRENCY= "eur"
    const val PREFERENCES_CURRENCY = "currency"
    const val PREFERENCES_CURRENCY_ID = "currencyId"

}