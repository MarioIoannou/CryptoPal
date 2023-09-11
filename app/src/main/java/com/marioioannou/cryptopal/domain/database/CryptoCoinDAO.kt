package com.marioioannou.cryptopal.domain.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoCoinDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity)

    @Query("SELECT * FROM crypto_coin_table ORDER BY id ASC")
    fun readCryptoCoins(): Flow<List<CryptoCoinEntity>>

    @Update
    suspend fun updateCryptoData(cryptoCoinEntity: CryptoCoinEntity)

    @Delete
    suspend fun deleteCryptoCoin(cryptoCoinEntity: CryptoCoinEntity)

    @Query("DELETE FROM crypto_coin_table")
    suspend fun deleteAllCryptoCoins()
}