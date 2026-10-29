package com.example.project_try9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_try9.data.Course
import com.example.project_try9.data.CourseRepository
import com.example.project_try9.data.CourseTopic
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CourseUiState(
    val courses: List<Course> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val selectedCourse: Course? = null,
    val selectedTopic: CourseTopic? = null,
    val detailLoading: Boolean = false
)

class CourseViewModel : ViewModel() {

    private val repository = CourseRepository()

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> =
        _uiState.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )

            try {
                val courses = repository.getCourses()
                _uiState.value = _uiState.value.copy(
                    courses = courses,
                    loading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.localizedMessage
                        ?: "Unable to load courses."
                )
            }
        }
    }

    fun selectCourseBySlug(slug: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(detailLoading = true, selectedTopic = null)
            try {
                val course = repository.getCourseBySlug(slug)
                _uiState.value = _uiState.value.copy(
                    selectedCourse = course,
                    detailLoading = false
                )
            } catch (e: Exception) {
                val localCourse = _uiState.value.courses.find { it.slug == slug }
                _uiState.value = _uiState.value.copy(
                    selectedCourse = localCourse,
                    detailLoading = false
                )
            }
        }
    }

    fun selectTopicBySlug(courseSlug: String, topicSlug: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(detailLoading = true)
            try {
                // Try fetching from https://tech3space.com/api/courses/topics/{slug}/
                val topic = repository.getTopicDetailBySlug(topicSlug)
                _uiState.value = _uiState.value.copy(
                    selectedTopic = topic,
                    detailLoading = false
                )
            } catch (e: Exception) {
                try {
                    // Fallback to https://tech3space.com/api/courses/{courseSlug}/{topicSlug}/
                    val topic = repository.getTopicBySlug(courseSlug, topicSlug)
                    _uiState.value = _uiState.value.copy(
                        selectedTopic = topic,
                        detailLoading = false
                    )
                } catch (e2: Exception) {
                    val localTopic = _uiState.value.selectedCourse?.topics?.find { it.slug == topicSlug }
                    _uiState.value = _uiState.value.copy(
                        selectedTopic = localTopic,
                        detailLoading = false
                    )
                }
            }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedCourse = null,
            selectedTopic = null
        )
    }

    fun clearSelectedTopic() {
        _uiState.value = _uiState.value.copy(selectedTopic = null)
    }
}
