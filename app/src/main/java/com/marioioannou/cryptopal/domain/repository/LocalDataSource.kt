package com.marioioannou.cryptopal.domain.repository

import android.util.Log
import com.marioioannou.cryptopal.data.datastore.ProtoRepository
import com.marioioannou.cryptopal.domain.api.CoinStatsApi
import com.marioioannou.cryptopal.domain.database.CryptoCoinDAO
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.database.crypto_watchlist.CryptoWatchlistEntity
import com.marioioannou.cryptopal.utils.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import com.marioioannou.cryptopal.domain.model.coins.Result as Result

class LocalDataSource @Inject constructor(
    private val cryptoCoinDAO: CryptoCoinDAO,
    private val coinStatsApi: CoinStatsApi,
    private val datastoreRepo: ProtoRepository,
) {

    // - Crypto Coins - //
    fun readCryptoCoins(): Flow<List<CryptoCoinEntity>> {
        return cryptoCoinDAO.readCryptoCoins()
    }

    suspend fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity) {
        return cryptoCoinDAO.insertCryptoCoin(cryptoCoinEntity)
    }

    suspend fun getCryptoCoinFromDatabase(coinId: String): CryptoCoinEntity? {
        return cryptoCoinDAO.getCoinById(coinId)
    }

    // - Crypto Watchlist - //
    fun readCryptoWatchlist(): Flow<List<CryptoWatchlistEntity>> {
        return cryptoCoinDAO.readCryptoWatchlist()
    }

    suspend fun insertCryptoWatchlist(cryptoWatchlistEntity: CryptoWatchlistEntity) {
        return cryptoCoinDAO.insertCryptoWatchlist(cryptoWatchlistEntity)
    }

    suspend fun deleteCryptoWatchlist(cryptoCoinEntity: CryptoWatchlistEntity) {
        return cryptoCoinDAO.deleteCryptoWatchlist(cryptoCoinEntity)
    }

    suspend fun deleteAllCryptoWatchlist() {
        return cryptoCoinDAO.deleteAllCryptoWatchlist()
    }

    suspend fun isWatchlistEmpty(): Boolean {
        return cryptoCoinDAO.readCryptoWatchlistCount() == 0
    }

    suspend fun updateCryptoCoinsData() {

        withContext(Dispatchers.IO) {

            val savedCoins = readCryptoWatchlist().first()

            for (savedCoin in savedCoins) {
                val coinId = savedCoin.cryptoCoin.id
                if (coinId != null) {
                    val response = coinStatsApi.getCryptoCoin(coinId, getCurrency())
                    if (response.isSuccessful) {
                        val updatedCoin = response.body()
                        if (updatedCoin != null) {
                            val coinEntity =
                                CryptoWatchlistEntity(id = savedCoin.id, cryptoCoin = updatedCoin)
                            cryptoCoinDAO.updateCryptoWatchlistData(coinEntity)
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

    private fun getCurrency():String = runBlocking {
        Log.d("LocalDataSource","Currency: ${datastoreRepo.readCurrency.first()}")
        return@runBlocking datastoreRepo.readCurrency.first()
    }
}