package io.sadwhy.party.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.sadwhy.party.data.model.Creator
import io.sadwhy.party.data.repository.CreatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatorViewModel : ViewModel() {
    private val api = CreatorRepository()

    val creator: MutableStateFlow<Creator?> = MutableStateFlow(null)

    fun fetchCreator(service: String, id: String) {
        viewModelScope.launch {
            creator.value = null
            val response = api.getCreator(service, id)
            if (response.isSuccessful) {
                creator.value = response.body()
            }
        }
    }

}