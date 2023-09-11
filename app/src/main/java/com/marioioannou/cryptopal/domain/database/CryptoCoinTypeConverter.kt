package com.marioioannou.cryptopal.domain.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marioioannou.cryptopal.domain.model.coins.Coin

class CryptoCoinTypeConverter {

    var gson = Gson()
    @TypeConverter
    fun cryptoCoinToString(cryptoCoin: Coin):String {
        return gson.toJson(cryptoCoin)
    }

    @TypeConverter
    fun stringToCryptoCoin(data: String): Coin {
        val listType = object : TypeToken<Coin>(){}.type
        return gson.fromJson(data,listType)
    }
}