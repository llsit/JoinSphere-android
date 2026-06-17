package com.llsit.joinsphere.feature.auth

sealed interface AuthUiEffect {
    data class ShowToast(val message: String) : AuthUiEffect

    object NavigateToHome : AuthUiEffect
}