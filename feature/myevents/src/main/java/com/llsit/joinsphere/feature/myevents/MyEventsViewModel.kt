package com.llsit.joinsphere.feature.myevents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.usecase.GetHostingEventsUseCase
import com.llsit.joinsphere.core.domain.usecase.GetPastEventsUseCase
import com.llsit.joinsphere.core.domain.usecase.GetSavedEventsUseCase
import com.llsit.joinsphere.core.domain.usecase.GetUpcomingEventsUseCase
import com.llsit.joinsphere.feature.myevents.state.MyEventsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyEventsViewModel(
    private val getHostingEventsUseCase: GetHostingEventsUseCase,
    private val getUpcomingEventsUseCase: GetUpcomingEventsUseCase,
    private val getSavedEventsUseCase: GetSavedEventsUseCase,
    private val getPastEventsUseCase: GetPastEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    init {
        loadHostingEvents()
        loadUpcomingEvents()
        loadSavedEvents()
        loadPastEvents()
    }

    fun loadUpcomingEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUpcoming = true) }
            getUpcomingEventsUseCase()
                .onSuccess { response ->
                    _uiState.update { 
                        it.copy(
                            todayEvents = response.today,
                            tomorrowEvents = response.tomorrow,
                            thisWeekEvents = response.thisWeek,
                            laterEvents = response.later,
                            isLoadingUpcoming = false
                        ) 
                    }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "Error loading upcoming events: ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoadingUpcoming = false) }
                }
        }
    }

    fun loadHostingEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHosting = true) }
            getHostingEventsUseCase()
                .onSuccess { events ->
                    _uiState.update { it.copy(hostingEvents = events, isLoadingHosting = false) }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "MyEventsViewModel Error : ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoadingHosting = false) }
                }
        }
    }

    fun loadSavedEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSaved = true) }
            getSavedEventsUseCase()
                .onSuccess { events ->
                    _uiState.update { it.copy(savedEvents = events, isLoadingSaved = false) }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "Error loading saved events: ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoadingSaved = false) }
                }
        }
    }

    fun loadPastEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPast = true) }
            getPastEventsUseCase()
                .onSuccess { events ->
                    _uiState.update {
                        it.copy(
                            pastEvents = events,
                            isLoadingPast = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("MyEventsViewModel", "Error loading past events: ${error.message}")
                    _uiState.update {
                        it.copy(
                            error = error.message,
                            isLoadingPast = false
                        )
                    }
                }
        }
    }
}
