package com.llsit.joinsphere.feature.profile.state

sealed interface ProfileIntent {

    data class Profile(val email: String, val password: String) : ProfileIntent
}