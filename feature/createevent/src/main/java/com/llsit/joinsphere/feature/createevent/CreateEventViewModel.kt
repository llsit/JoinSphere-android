package com.llsit.joinsphere.feature.createevent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.usecase.CreateEventUseCase
import com.llsit.joinsphere.core.model.EventDto
import com.llsit.joinsphere.feature.createevent.state.CreateEventIntent
import com.llsit.joinsphere.feature.createevent.state.CreateEventUiEffect
import com.llsit.joinsphere.feature.createevent.state.CreateEventUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventViewModel(
    private val createEventUseCase: CreateEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventUiEffect>()
    val effect: SharedFlow<CreateEventUiEffect> = _effect.asSharedFlow()

    fun processIntent(intent: CreateEventIntent) {
        when (intent) {
            is CreateEventIntent.NextStep -> handleNextStep()
            is CreateEventIntent.PreviousStep -> _uiState.update {
                it.copy(
                    currentStep = (it.currentStep - 1).coerceAtLeast(
                        1
                    )
                )
            }

            is CreateEventIntent.ResetForm -> _uiState.value = CreateEventUiState()
            is CreateEventIntent.PublishEvent -> publishEvent()

            is CreateEventIntent.UpdateTitle -> _uiState.update { it.copy(title = intent.title) }
            is CreateEventIntent.UpdateCategory -> _uiState.update { it.copy(categoryId = intent.categoryId) }
            is CreateEventIntent.UpdateDescription -> _uiState.update { it.copy(description = intent.description) }
            is CreateEventIntent.UpdateSoloFriendly -> _uiState.update { it.copy(isSoloFriendly = intent.isSoloFriendly) }
            is CreateEventIntent.ImageSelected -> _uiState.update { it.copy(localImageUri = intent.uri) }
            is CreateEventIntent.UpdateDate -> _uiState.update { it.copy(date = intent.date) }
            is CreateEventIntent.UpdateTime -> _uiState.update { it.copy(time = intent.time) }
            is CreateEventIntent.UpdateLocation -> _uiState.update { it.copy(location = intent.location) }
            is CreateEventIntent.UpdateMaxAttendees -> _uiState.update { it.copy(maxAttendees = intent.maxAttendees) }
            is CreateEventIntent.UpdateIsFree -> _uiState.update { it.copy(isFree = intent.isFree) }
            is CreateEventIntent.UpdatePrice -> _uiState.update { it.copy(price = intent.price) }
        }
    }

    private fun handleNextStep() {
        _uiState.update {
            if (it.currentStep < 3) it.copy(currentStep = it.currentStep + 1) else it
        }
    }

    private fun publishEvent() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true) }

            try {
                val eventDto = EventDto(
                    title = currentState.title,
                    categoryId = currentState.categoryId,
                    description = currentState.description,
                    date = currentState.date,
                    time = currentState.time,
                    location = currentState.location,
                    maxAttendees = currentState.maxAttendees.toIntOrNull(),
                    isFree = currentState.isFree,
                    price = currentState.price.toDoubleOrNull() ?: 0.0,
                    isSoloFriendly = currentState.isSoloFriendly
                )

                createEventUseCase(
                    eventData = eventDto,
                    localImageUri = currentState.localImageUri
                ).getOrThrow()

                _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
                _effect.emit(CreateEventUiEffect.ShowToast("สร้างกิจกรรมสำเร็จ!"))
            } catch (e: Exception) {
                Log.e(
                    "CreateEventViewModel",
                    "Error publishing event + ${e.localizedMessage ?: "Publish Failed"}",
                    e
                )
                _uiState.update { it.copy(isLoading = false) }
                _effect.emit(CreateEventUiEffect.ShowToast(e.localizedMessage ?: "Publish Failed"))
            }
        }
    }

    fun dispatchGalleryEffect() {
        viewModelScope.launch { _effect.emit(CreateEventUiEffect.OpenGallery) }
    }

    fun dispatchMapEffect() {
        viewModelScope.launch { _effect.emit(CreateEventUiEffect.OpenMap) }
    }
}