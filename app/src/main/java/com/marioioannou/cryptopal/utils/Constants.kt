package com.marioioannou.cryptopal.utils

import com.marioioannou.cryptopal.BuildConfig

object Constants {
    //https://api.coingecko.com/api/v3/coins/
    const val BASE_URL = "https://api.coingecko.com/"
    const val BASE_URL_NEWS = "https://api.coinstats.app/"
    const val API_KEY = BuildConfig.API_KEY

    const val PREFERENCES_BACK_ONLINE = "backOnline"

    //API QUERY - COINS
    const val QUERY_COINS_CURRENCY = "vs_currency"
    const val QUERY_COINS_NUMBER = "per_page"
    const val QUERY_COINS_PAGE = "page"
    const val QUERY_COINS_SPARKLINE = "sparkline"
    const val QUERY_COINS_PRICE_CHANGE_PERCENTAGE = "price_change_percentage"

    //API QUERY - TRENDING COINS

    //API QUERY - SEARCHING COINS

    //API QUERY - COIN CHART

    //API QUERY - NEWS

    //ROOM
    const val DATABASE_NAME = "crypto_watchlist_database"
    const val CRYPTO_COIN_TABLE = "crypto_coin_table"

    //DATASTORE
    const val PREFERENCES_NAME = "crypto_coin_preferences"

    const val DEFAULT_CURRENCY= "usd"
    const val DEFAULT_THEME= "system default"
    const val DEFAULT_FILTER= "market_cap_desc"

    const val PREFERENCES_CURRENCY = "currency"
    const val PREFERENCES_CURRENCY_ID = "currencyId"
    const val PREFERENCES_THEME = "theme"
    const val PREFERENCES_THEME_ID = "themeId"
    const val PREFERENCES_FILTER = "filter"
    const val PREFERENCES_FILTER_ID = "filterId"
}