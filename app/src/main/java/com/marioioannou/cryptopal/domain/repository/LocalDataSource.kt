package com.marioioannou.cryptopal.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marioioannou.cryptopal.domain.database.CryptoCoinDAO
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cryptoCoinDAO: CryptoCoinDAO
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

}