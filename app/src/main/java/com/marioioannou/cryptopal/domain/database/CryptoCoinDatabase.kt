package com.marioioannou.cryptopal.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CryptoCoinEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CryptoCoinTypeConverter::class)
abstract class CryptoCoinDatabase: RoomDatabase()  {

    abstract fun cryptoCoinDAO(): CryptoCoinDAO

}