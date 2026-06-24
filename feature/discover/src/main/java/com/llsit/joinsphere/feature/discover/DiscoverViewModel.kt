package com.llsit.joinsphere.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.usecase.GetCurrentLocationUseCase
import com.llsit.joinsphere.core.domain.usecase.GetDiscoverFeedsUseCase
import com.llsit.joinsphere.core.model.Category
import com.llsit.joinsphere.core.model.EventNetworkModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val categories: List<Category> = emptyList(),
    val trending: List<EventNetworkModel> = emptyList(),
    val thisWeek: List<EventNetworkModel> = emptyList(),
    val address: String = "ดึงข้อมูลตำแหน่ง...",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface DiscoverIntent {
    data object Refresh : DiscoverIntent
}

class DiscoverViewModel(
    private val getDiscoverFeedsUseCase: GetDiscoverFeedsUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
        fetchFeeds()
    }

    private fun observeCategories() {
        eventRepository.getCategories()
            .onEach { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: DiscoverIntent) {
        when (intent) {
            DiscoverIntent.Refresh -> fetchFeeds()
        }
    }

    private fun fetchFeeds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val userLocation = getCurrentLocationUseCase()
            _uiState.update { it.copy(address = userLocation?.address ?: "Bangkok") }

            getDiscoverFeedsUseCase(
                lat = userLocation?.lat ?: 13.7563,
                lng = userLocation?.lng ?: 100.5018,
                radius = 20000.0
            )
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            trending = response.trending,
                            thisWeek = response.thisWeek
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
}
