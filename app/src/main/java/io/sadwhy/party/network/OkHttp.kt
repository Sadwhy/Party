package io.sadwhy.party.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttp {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            // TODO: interceptors, logging etc
            .build()
    }
}