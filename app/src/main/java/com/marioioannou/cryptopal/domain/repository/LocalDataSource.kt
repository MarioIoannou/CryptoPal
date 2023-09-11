package com.marioioannou.cryptopal.domain.repository

import android.util.Log
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marioioannou.cryptopal.domain.api.CoinStatsApi
import com.marioioannou.cryptopal.domain.database.CryptoCoinDAO
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cryptoCoinDAO: CryptoCoinDAO,
    private val coinStatsApi: CoinStatsApi,
) {

    fun readCryptoCoins(): Flow<List<CryptoCoinEntity>>{
        return cryptoCoinDAO.readCryptoCoins()
    }

    suspend fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity){
        return cryptoCoinDAO.insertCryptoCoin(cryptoCoinEntity)
    }

    suspend fun deleteCryptoCoin(cryptoCoinEntity: CryptoCoinEntity){
        return cryptoCoinDAO.deleteCryptoCoin(cryptoCoinEntity)
    }

    suspend fun deleteAllCryptoCoins(){
        return cryptoCoinDAO.deleteAllCryptoCoins()
    }

    suspend fun updateCryptoCoinsData() {
        withContext(Dispatchers.IO) {

            val savedCoins = readCryptoCoins().first()

            for (savedCoin in savedCoins) {
                val coinId = savedCoin.cryptoCoin.coin_id
                if (coinId != null) {
                    val response = coinStatsApi.getCryptoCoin(coinId, "USD")
                    if (response.isSuccessful) {
                        val updatedCoin = response.body()
                        if (updatedCoin != null) {
                            val coinEntity = CryptoCoinEntity(id = savedCoin.id, cryptoCoin = updatedCoin.coin)
                            cryptoCoinDAO.updateCryptoData(coinEntity)
                        }
                    } else {
                        Log.e(
                            "CoinRepository",
                            "Failed to update coin with ID $coinId: ${response.errorBody()}"
                        )
                    }
                }
            }
        }
    }

}