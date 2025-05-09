package io.sadwhy.party.network

import kotlinx.serialization.json.Json
import io.sadwhy.party.network.OkHttp
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://kemono.su/api"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val contentType = "application/json; charset=UTF-8".toMediaType()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttp.client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}