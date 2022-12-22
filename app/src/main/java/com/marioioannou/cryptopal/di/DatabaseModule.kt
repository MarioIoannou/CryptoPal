package com.marioioannou.cryptopal.di

import android.content.Context
import androidx.room.Room
import com.marioioannou.cryptopal.domain.database.CryptoCoinDatabase
import com.marioioannou.cryptopal.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        CryptoCoinDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(database: CryptoCoinDatabase) = database.cryptoCoinDAO()
}