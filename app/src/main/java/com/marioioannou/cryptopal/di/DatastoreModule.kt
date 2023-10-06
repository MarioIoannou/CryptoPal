package com.marioioannou.cryptopal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.marioioannou.cryptopal.UserPreferences
import com.marioioannou.cryptopal.data.datastore.ProtoRepository
import com.marioioannou.cryptopal.data.datastore.UserPreferencesSerializer
import com.marioioannou.cryptopal.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

//    @Singleton
//    @Provides
//    fun provideDatastoreRepo(
//        @ApplicationContext context: Context
//    ): DatastoreRepo = DatastoreRepoImpl(context)

//    @Singleton
//    @Provides
//    fun provideUserPreferences(
//        @ApplicationContext context: Context,
//        dataStore: DataStore<UserPreferences>
//    ): ProtoRepository = ProtoRepository(context,dataStore)

    @Singleton
    @Provides
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        serializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> {
        val dataStoreFile: File = context.dataStoreFile(Constants.PREFERENCES_NAME)
        return DataStoreFactory.create(
            serializer = serializer,
            produceFile = { dataStoreFile }
        )
    }

    @Singleton
    @Provides
    fun provideUserPreferencesSerializer(): UserPreferencesSerializer = UserPreferencesSerializer


}