package com.llsit.joinsphere.feature.eventdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.feature.eventdetail.state.EventDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    private fun observeCategories() {
        eventRepository.getCategories()
            .onEach { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
            .launchIn(viewModelScope)
    }

    fun loadEventDetail(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            eventRepository.getEventDetail(eventId)
                .onSuccess { event ->
                    _uiState.update { it.copy(event = event) }
                    fetchHostProfile(event.creatorId)
                    checkAttendingStatus(eventId)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun checkAttendingStatus(eventId: String) {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            eventRepository.isUserAttending(eventId, userId)
                .onSuccess { isAttending ->
                    _uiState.update { it.copy(isAttending = isAttending) }
                }
        }
    }

    fun joinEvent() {
        val eventId = uiState.value.event?.id ?: return
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isJoining = true, error = null) }
            eventRepository.joinEvent(eventId, userId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isJoining = false,
                            isAttending = true,
                            event = state.event?.copy(
                                attendeeCount = (state.event.attendeeCount ?: 0) + 1
                            )
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("EventDetailViewModel", "EventDetailViewModel Error : ${e.message}")
                    _uiState.update { it.copy(isJoining = false, error = e.message) }
                }
        }
    }

    fun cancelJoinEvent() {
        val eventId = uiState.value.event?.id ?: return
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isJoining = true, error = null) }
            eventRepository.cancelJoinEvent(eventId, userId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isJoining = false,
                            isAttending = false,
                            event = state.event?.copy(
                                attendeeCount = (state.event.attendeeCount ?: 0).minus(1).coerceAtLeast(0)
                            )
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("EventDetailViewModel", "CancelJoin Error : ${e.message}")
                    _uiState.update { it.copy(isJoining = false, error = e.message) }
                }
        }
    }

    private suspend fun fetchHostProfile(hostId: String) {
        profileRepository.getUserProfile(hostId)
            .onSuccess { host ->
                _uiState.update { it.copy(host = host, isLoading = false) }
            }
            .onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
    }
}
