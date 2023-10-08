package com.marioioannou.cryptopal.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.database.crypto_watchlist.CryptoWatchlistEntity

@Database(
    entities = [CryptoCoinEntity::class , CryptoWatchlistEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(CryptoCoinTypeConverter::class)
abstract class CryptoCoinDatabase: RoomDatabase()  {

    abstract fun cryptoCoinDAO(): CryptoCoinDAO

}