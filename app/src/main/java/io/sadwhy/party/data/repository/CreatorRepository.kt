package io.sadwhy.party.data.repository

import android.util.Log
import io.sadwhy.party.data.api.CreatorService
import io.sadwhy.party.data.model.Creator
import io.sadwhy.party.network.RetrofitClient
import retrofit2.Response

class CreatorRepository {
    private val creatorService = RetrofitClient.retrofit.create(CreatorService::class.java)

    suspend fun getAllCreators(): Response<List<Creator>> {
        return creatorService.getAllCreators()
    }

    suspend fun getCreator(service: String, creatorId: String): Response<Creator> {
        return creatorService.getCreator(service, creatorId)
    }
}