package com.marioioannou.cryptopal.domain.database

import androidx.room.*
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.database.crypto_watchlist.CryptoWatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoCoinDAO {

    // - Crypto Coins - //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity)

    @Query("SELECT * FROM crypto_coins_table ORDER BY id ASC")
    fun readCryptoCoins(): Flow<List<CryptoCoinEntity>>

    @Query("SELECT * FROM crypto_coins_table WHERE id = :coinId")
    suspend fun getCoinById(coinId: String): CryptoCoinEntity?

    // - Crypto Watchlist - //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoWatchlist(cryptoCoinEntity: CryptoWatchlistEntity)

    @Query("SELECT * FROM crypto_watchlist_table ORDER BY id ASC")
    fun readCryptoWatchlist(): Flow<List<CryptoWatchlistEntity>>

    @Update
    suspend fun updateCryptoWatchlistData(cryptoCoinEntity: CryptoWatchlistEntity)

    @Delete
    suspend fun deleteCryptoWatchlist(cryptoCoinEntity: CryptoWatchlistEntity)

    @Query("DELETE FROM crypto_watchlist_table")
    suspend fun deleteAllCryptoWatchlist()
}