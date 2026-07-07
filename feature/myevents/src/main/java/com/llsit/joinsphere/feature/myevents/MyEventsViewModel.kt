package com.llsit.joinsphere.feature.myevents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.usecase.GetHostingEventsUseCase
import com.llsit.joinsphere.core.domain.usecase.GetUpcomingEventsUseCase
import com.llsit.joinsphere.feature.myevents.state.MyEventsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyEventsViewModel(
    private val getHostingEventsUseCase: GetHostingEventsUseCase,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    init {
        loadHostingEvents()
        loadUpcomingEvents()
    }

    fun loadUpcomingEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUpcomingEventsUseCase()
                .onSuccess { response ->
                    _uiState.update { 
                        it.copy(
                            todayEvents = response.today,
                            tomorrowEvents = response.tomorrow,
                            thisWeekEvents = response.thisWeek,
                            laterEvents = response.later,
                            isLoading = false
                        ) 
                    }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "Error loading upcoming events: ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun loadHostingEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getHostingEventsUseCase()
                .onSuccess { events ->
                    _uiState.update { it.copy(hostingEvents = events, isLoading = false) }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "MyEventsViewModel Error : ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}
