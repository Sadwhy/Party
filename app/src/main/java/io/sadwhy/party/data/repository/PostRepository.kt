package io.sadwhy.party.data.repository

import android.util.Log
import io.sadwhy.party.data.api.PostService
import io.sadwhy.party.data.model.PostRecent
import io.sadwhy.party.data.model.PostResponse
import io.sadwhy.party.network.RetrofitClient
import retrofit2.Response

class PostRepository {
    private val postService = RetrofitClient.retrofit.create(PostService::class.java)

    suspend fun getRecentPosts(): Response<PostRecent> {
        return postService.getRecentPosts()
    }

    suspend fun getPost(service: String, creatorId: String, postId: String): Response<PostResponse> {
        return postService.getPost(service, creatorId, postId)
    }
}