package com.marioioannou.cryptopal.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.marioioannou.cryptopal.domain.api.AuthInterceptor
import com.marioioannou.cryptopal.domain.api.CoinGeckoApi
import com.marioioannou.cryptopal.domain.api.CoinStatsApi
import com.marioioannou.cryptopal.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Singleton
    @Provides
    @Named("WithAuth")
    fun provideOkHttpClientWithAuth(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("WithoutAuth")
    fun provideOkHttpClientWithoutAuth(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("CoinStats")
    fun provideCoinStatsInstance(
        @Named("WithAuth") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_COINSTATS)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named("CoinGecko")
    fun provideCoinGeckoInstance(
        @Named("WithoutAuth") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_COINGECKO)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinStatsApi(@Named("CoinStats") retrofit: Retrofit): CoinStatsApi {
        return retrofit.create(CoinStatsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoinGeckoApi(@Named("CoinGecko") retrofit: Retrofit): CoinGeckoApi {
        return retrofit.create(CoinGeckoApi::class.java)
    }
}