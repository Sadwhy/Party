package io.sadwhy.party.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import io.sadwhy.party.data.model.Creator

interface CreatorService {
    @GET("v1/creators.txt")
    suspend fun getAllCreators(): Response<List<Creator>>


    @GET("v1/{service}/user/{creator_id}/profile")
    suspend fun getCreator(
        @Path("service") service: String,
        @Path("creator_id") creatorId: String
    ): Response<Creator>
}