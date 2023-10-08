package com.marioioannou.cryptopal.domain.api

import com.marioioannou.cryptopal.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("X-API-KEY", Constants.QUERY_COINSTATS_API_KEY)
            .build()

        return chain.proceed(request)
    }
}