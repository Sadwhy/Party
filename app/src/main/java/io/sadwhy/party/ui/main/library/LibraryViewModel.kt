package io.sadwhy.party.ui.main.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import io.sadwhy.party.data.model.Recent
import io.sadwhy.party.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val postRepository = PostRepository()

    val posts: MutableStateFlow<Recent?>(null)

    fun fetchPosts() {
        viewModelScope.launch {
            posts.value = null
            val response = postRepository.getRecentPosts()
            if (response.isSuccessful) {
                val recent = response.body()
                posts.value = recent
            }
        }
    }

    init {
        fetchPosts()
    }
}