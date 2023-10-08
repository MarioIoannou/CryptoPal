package com.marioioannou.cryptopal.domain.database.crypto_watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marioioannou.cryptopal.domain.model.coins.Result
import com.marioioannou.cryptopal.utils.Constants

@Entity(tableName = Constants.CRYPTO_WATCHLIST_TABLE)
data class CryptoWatchlistEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cryptoCoin: Result
){

}
