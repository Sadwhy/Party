package io.sadwhy.party.data.repository

import android.util.Log
import io.sadwhy.party.data.api.CreatorService
import io.sadwhy.party.data.model.Creator
import io.sadwhy.party.network.RetrofitClient
import retrofit2.Response

class CreatorRepository {
    private val service = RetrofitClient.retrofit.create(CreatorService::class.java)

    suspend fun getAllCreator(): Response<List<Creator>> {
        return service.getAllCreator()
    }

    suspend fun getCreator(service: String, creatorId: String): Response<Creator> {
        return service.getCreator(service, creatorId)
    }
}