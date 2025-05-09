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

    // Add loading state to track network request status
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Add error state to handle exceptions
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // The suspend function will properly wait here
                val response = postRepository.getRecentPosts()
                
                if (response.isSuccessful) {
                    log("Got posts in ViewModel: ${response.body()?.posts?.size ?: 0} posts")
                    _posts.value = response.body()?.posts ?: emptyList()
                } else {
                    log("Error fetching posts: ${response.code()} - ${response.message()}")
                    _error.value = "Failed to load posts: ${response.message()}"
                    // Keep the current posts (don't set to empty)
                }
            } catch (e: Exception) {
                log("Exception fetching posts: ${e.message}")
                _error.value = "Error: ${e.message}"
                // Keep the current posts (don't set to empty)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Call this method in onViewCreated or when the screen becomes visible
    init {
        fetchPosts()
    }
}