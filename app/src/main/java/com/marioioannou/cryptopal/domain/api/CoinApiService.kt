package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinApiService {

        private val coin_api by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_NEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val coinApi: CryptoApi by lazy {
            coin_api.create(CryptoApi::class.java)
        }
}