package io.sadwhy.party.screen.creator

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

    private val _creator = MutableStateFlow<Creator?>(null)
    val creator: StateFlow<Creator?> = _creator.asStateFlow()

    private var currentService: String? = null
    private var currentId: String? = null

    fun fetchCreator(service: String, id: String) {
        if (service == currentService && id == currentId && _creator.value != null) {
            return
        }

        currentService = service
        currentId = id

        viewModelScope.launch {
            _creator.value = null
            val response = api.getCreator(service, id)
            if (response.isSuccessful) {
                _creator.value = response.body()
            } else {
                _creator.value = null
            }
        }
    }
}