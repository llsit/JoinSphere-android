package com.llsit.joinsphere.feature.auth

sealed interface AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent
    data class Register(val email: String, val password: String) : AuthIntent
    object Logout : AuthIntent
    object ClearError : AuthIntent
}