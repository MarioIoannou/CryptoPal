package com.marioioannou.cryptopal.domain.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marioioannou.cryptopal.domain.model.coins.Coin
import com.marioioannou.cryptopal.utils.Constants.CRYPTO_COIN_TABLE

@Entity(tableName = CRYPTO_COIN_TABLE)
data class CryptoCoinEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var cryptoCoin: Coin
)
