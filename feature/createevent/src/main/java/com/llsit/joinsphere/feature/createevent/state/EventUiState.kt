package com.llsit.joinsphere.feature.createevent.state

import com.llsit.joinsphere.core.model.Category
import com.llsit.joinsphere.core.model.event.SelectedPlace
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class CreateEventUiState(
    val currentStep: Int = 1,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null,

    val title: String = "",
    val categoryId: String = "",
    val categories: List<Category> = emptyList(),
    val description: String = "",
    val date: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val time: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
    val selectedPlace: SelectedPlace = SelectedPlace(),
    val maxAttendees: String = "",
    val isFree: Boolean = true,
    val price: String = "",
    val isSoloFriendly: Boolean = true,
    val localImageUri: android.net.Uri? = null
) {
    val isStep1Complete: Boolean get() = title.trim().length > 3 && categoryId.isNotEmpty()
    val isStep2Complete: Boolean
        get() = date.isNotEmpty() && time.isNotEmpty() && selectedPlace.address.trim().isNotEmpty()

    val isNextButtonEnabled: Boolean
        get() = when (currentStep) {
            1 -> isStep1Complete
            2 -> isStep2Complete
            3 -> true
            else -> false
        }
}

sealed interface CreateEventIntent {
    object NextStep : CreateEventIntent
    object PreviousStep : CreateEventIntent
    object ResetForm : CreateEventIntent
    object PublishEvent : CreateEventIntent

    data class UpdateTitle(val title: String) : CreateEventIntent
    data class UpdateCategory(val categoryId: String) : CreateEventIntent
    data class UpdateDescription(val description: String) : CreateEventIntent
    data class UpdateSoloFriendly(val isSoloFriendly: Boolean) : CreateEventIntent
    data class ImageSelected(val uri: android.net.Uri) : CreateEventIntent

    data class UpdateDate(val date: String) : CreateEventIntent
    data class UpdateTime(val time: String) : CreateEventIntent
    data class UpdateLocation(val selectedPlace: SelectedPlace) : CreateEventIntent
    data class UpdateMaxAttendees(val maxAttendees: String) : CreateEventIntent
    data class UpdateIsFree(val isFree: Boolean) : CreateEventIntent
    data class UpdatePrice(val price: String) : CreateEventIntent
}

sealed interface CreateEventUiEffect {
    object OpenGallery : CreateEventUiEffect
    object OpenCamera : CreateEventUiEffect
    object OpenMap : CreateEventUiEffect
    data class ShowToast(val message: String) : CreateEventUiEffect
}