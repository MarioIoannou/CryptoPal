package com.marioioannou.cryptopal.domain.database.crypto_coins

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.utils.Constants.CRYPTO_COIN_TABLE

@Entity(tableName = CRYPTO_COIN_TABLE)
data class CryptoCoinEntity(
    var cryptoCoin: CryptoCoins
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
