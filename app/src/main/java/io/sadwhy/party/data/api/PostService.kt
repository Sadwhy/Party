package io.sadwhy.party.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import io.sadwhy.party.data.model.PostRecent
import io.sadwhy.party.data.model.PostResponse

interface PostService {
    @GET("v1/posts")
    suspend fun getRecentPosts(): Response<PostRecent>

    @GET("v1/{service}/user/{creator_id}/post/{post_id}")
    suspend fun getPost(
        @Path("service") service: String,
        @Path("creator_id") creatorId: String,
        @Path("post_id") postId: String
    ): Response<PostResponse>
}