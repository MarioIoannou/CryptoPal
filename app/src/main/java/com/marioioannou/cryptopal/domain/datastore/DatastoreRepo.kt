package com.marioioannou.cryptopal.domain.datastore

interface DatastoreRepo {
    suspend fun putCurrency(key: String,value: String)
    suspend fun getCurrency(key: String): String?

    suspend fun putThemeMode(key: String,value: String)
    suspend fun getThemeMode(key: String): String?

    suspend fun putFilter(key: String,value: String)
    suspend fun getFilter(key: String): String?

    suspend fun clearPref(key: String)
}