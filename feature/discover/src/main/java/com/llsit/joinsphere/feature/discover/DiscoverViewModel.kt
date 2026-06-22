package com.llsit.joinsphere.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.usecase.GetDiscoverFeedsUseCase
import com.llsit.joinsphere.core.model.EventNetworkModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DiscoverUiState {
    data object Loading : DiscoverUiState
    data class Success(
        val trending: List<EventNetworkModel>,
        val thisWeek: List<EventNetworkModel>
    ) : DiscoverUiState
    data class Error(val message: String) : DiscoverUiState
}

sealed interface DiscoverIntent {
    data object Refresh : DiscoverIntent
}

class DiscoverViewModel(
    private val getDiscoverFeedsUseCase: GetDiscoverFeedsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        fetchFeeds()
    }

    fun onIntent(intent: DiscoverIntent) {
        when (intent) {
            DiscoverIntent.Refresh -> fetchFeeds()
        }
    }

    private fun fetchFeeds() {
        viewModelScope.launch {
            _uiState.update { DiscoverUiState.Loading }
            getDiscoverFeedsUseCase()
                .onSuccess { response ->
                    _uiState.update {
                        DiscoverUiState.Success(
                            trending = response.trending,
                            thisWeek = response.thisWeek
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        DiscoverUiState.Error(error.message ?: "Unknown error")
                    }
                }
        }
    }
}