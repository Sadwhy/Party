package io.sadwhy.party.ui.main.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.sadwhy.party.network.RetrofitClient
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.model.Recent
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecentPosts()
                if (response.isSuccessful) {
                    _posts.value = response.body()?.posts ?: emptyList()
                } else {
                    _posts.value = emptyList() // or handle error
                }
            } catch (e: Exception) {
                _posts.value = emptyList() // handle network failure
            }
        }
    }
}