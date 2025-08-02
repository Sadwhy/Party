package io.sadwhy.party.network

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://kemono.cr/api/"

    private val contentType = "application/json; charset=UTF-8".toMediaType()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttp.client)
            .addConverterFactory(JsonProvider.json.asConverterFactory(contentType))
            .build()
    }
}