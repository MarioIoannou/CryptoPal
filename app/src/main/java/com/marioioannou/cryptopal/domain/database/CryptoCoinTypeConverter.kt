package com.marioioannou.cryptopal.domain.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.domain.model.coins.Result

class CryptoCoinTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun cryptoCoinToString(cryptoCoin: Result):String {
        return gson.toJson(cryptoCoin)
    }

    @TypeConverter
    fun stringToCryptoCoin(data: String): Result {
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun cryptoCoinsToString(cryptoCoins: CryptoCoins):String {
        return gson.toJson(cryptoCoins)
    }

    @TypeConverter
    fun stringToCryptoCoins(data: String): CryptoCoins {
        val listType = object : TypeToken<CryptoCoins>(){}.type
        return gson.fromJson(data,listType)
    }
}