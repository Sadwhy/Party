package io.sadwhy.party.ui.main.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.sadwhy.party.utils.log
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val response = postRepository.getRecentPosts()
                _posts.value = if (response.isSuccessful) {
                    log("Got post in Viewmodel")
                    response.body()?.posts ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
        }
    }
}