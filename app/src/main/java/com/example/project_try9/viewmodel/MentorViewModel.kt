package com.example.project_try9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_try9.data.MentorProfile
import com.example.project_try9.data.MentorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MentorUiState(
    val mentors: List<MentorProfile> = emptyList(),
    val developers: List<MentorProfile> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class MentorViewModel : ViewModel() {
    private val repository = MentorRepository()

    private val _uiState = MutableStateFlow(MentorUiState())
    val uiState: StateFlow<MentorUiState> = _uiState.asStateFlow()

    init {
        loadCommunity()
    }

    fun loadCommunity() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val mentors = repository.getMentors()
                val developers = repository.getDevelopers()
                _uiState.value = MentorUiState(
                    mentors = mentors,
                    developers = developers,
                    loading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.localizedMessage ?: "Failed to load community profiles."
                )
            }
        }
    }
}
