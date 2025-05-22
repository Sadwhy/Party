package io.sadwhy.party.data.repository

import android.util.Log
import io.sadwhy.party.data.api.PostService
import io.sadwhy.party.data.model.Recent
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.network.RetrofitClient
import retrofit2.Response

class PostRepository {
    private val postService = RetrofitClient.retrofit.create(PostService::class.java)

    suspend fun getRecentPosts(): Response<Recent> {
        return postService.getRecentPosts()
    }

    suspend fun getPost(service: String, creatorId: String, postId: String): Response<Post> {
        return postService.getPost(service, creatorId, postId)
    }
}