package com.example.project_try9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_try9.data.FormulaCategory
import com.example.project_try9.data.FormulaItem
import com.example.project_try9.data.FormulaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FormulaUiState(
    val categories: List<FormulaCategory> = emptyList(),
    val selectedFormula: FormulaItem? = null,
    val loading: Boolean = false,
    val detailLoading: Boolean = false,
    val error: String? = null
)

class FormulaViewModel : ViewModel() {
    private val repository = FormulaRepository()

    private val _uiState = MutableStateFlow(FormulaUiState())
    val uiState: StateFlow<FormulaUiState> = _uiState.asStateFlow()

    init {
        loadFormulas()
    }

    fun loadFormulas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val categories = repository.getFormulas()
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    loading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.localizedMessage ?: "Failed to load formulas."
                )
            }
        }
    }

    fun selectFormulaBySlug(slug: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(detailLoading = true)
            try {
                val formula = repository.getFormulaDetailBySlug(slug)
                _uiState.value = _uiState.value.copy(
                    selectedFormula = formula,
                    detailLoading = false
                )
            } catch (e: Exception) {
                var found: FormulaItem? = null
                for (cat in _uiState.value.categories) {
                    val match = cat.formulas.find { it.slug == slug }
                    if (match != null) {
                        found = match
                        break
                    }
                }
                _uiState.value = _uiState.value.copy(
                    selectedFormula = found,
                    detailLoading = false
                )
            }
        }
    }

    fun clearSelectedFormula() {
        _uiState.value = _uiState.value.copy(selectedFormula = null)
    }
}
