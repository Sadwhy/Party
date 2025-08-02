package io.sadwhy.party.data.repository

import io.sadwhy.party.data.api.PostService
import io.sadwhy.party.data.model.ApiResult
import io.sadwhy.party.data.model.PostRecent
import io.sadwhy.party.data.model.PostResponse
import io.sadwhy.party.network.RetrofitClient
import retrofit2.Response

class PostRepository : BaseRepository() {
    private val postService = RetrofitClient.retrofit.create(PostService::class.java)

    suspend fun getRecentPosts(): Response<PostRecent> {
        return postService.getRecentPosts()
    }

    suspend fun getPost(service: String, creatorId: String, postId: String): ApiResult<PostResponse> {
        return call { postService.getPost(service, creatorId, postId) }
    }
}