package com.marioioannou.cryptopal.domain.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoin

class CryptoCoinTypeConverter {

    var gson = Gson()
    @TypeConverter
    fun cryptoCoinToString(cryptoCoin: CryptoCoin):String {
        return gson.toJson(cryptoCoin)
    }

    @TypeConverter
    fun stringToCryptoCoin(data: String): CryptoCoin {
        val listType = object : TypeToken<CryptoCoin>(){}.type
        return gson.fromJson(data,listType)
    }
}