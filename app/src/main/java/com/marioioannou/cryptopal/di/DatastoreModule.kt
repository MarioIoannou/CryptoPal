package com.marioioannou.cryptopal.di

import android.content.Context
import com.marioioannou.cryptopal.domain.datastore.DatastoreRepo
import com.marioioannou.cryptopal.domain.datastore.DatastoreRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Singleton
    @Provides
    fun provideDatastoreRepo(
        @ApplicationContext context: Context
    ):DatastoreRepo = DatastoreRepoImpl(context)
}