package io.sadwhy.party.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.sadwhy.party.data.model.ApiResult
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val api = PostRepository()

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post.asStateFlow()

    private var currentId: String? = null

    fun fetchPost(service: String, user: String, id: String) {
        if (id == currentId && _post.value != null) {
            return
        }

        currentId = id

        viewModelScope.launch {
            _post.value = null
            val response = api.getPost(service, user, id)
            when (response) {
                is ApiResult.Success -> {
                    _post.value = response.data.post
                }

                is ApiResult.Failure -> {
                    _post.value = null
                }

                is ApiResult.Exception -> {
                    throw response.throwable
                }

            }
        }

    }
}