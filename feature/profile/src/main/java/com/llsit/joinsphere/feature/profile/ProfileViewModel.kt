package com.llsit.joinsphere.feature.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.domain.usecase.UploadImageProfileUseCase
import com.llsit.joinsphere.feature.profile.state.ProfileIntent
import com.llsit.joinsphere.feature.profile.state.ProfileUiEffect
import com.llsit.joinsphere.feature.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val uploadImageProfileUseCase: UploadImageProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileUiEffect>()
    val effect: SharedFlow<ProfileUiEffect> = _effect.asSharedFlow()

    init {
        fetchProfile()
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.EditImageProfile -> handlePhotoSelected(intent.uri)
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            profileRepository.getUserProfile().onSuccess { profile ->
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message ?: "Unknown error", isLoading = false) }
            }
        }
    }

    private fun handlePhotoSelected(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            uploadImageProfileUseCase(uri).onSuccess { newUrl ->
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        profile = it.profile?.copy(avatarUrl = newUrl)
                    ) 
                }
                _effect.emit(ProfileUiEffect.ShowToast("อัปโหลดรูปโปรไฟล์สำเร็จ"))
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false) }
                _effect.emit(ProfileUiEffect.ShowToast("อัปโหลดล้มเหลว: ${e.message}"))
            }
        }
    }

    fun dispatchGalleryEffect() {
        viewModelScope.launch {
            _effect.emit(ProfileUiEffect.OpenGallery)
        }
    }
}