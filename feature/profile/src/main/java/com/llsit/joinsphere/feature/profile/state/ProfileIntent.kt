package com.llsit.joinsphere.feature.profile.state

import android.net.Uri

sealed interface ProfileIntent {
    data class EditImageProfile(val uri : Uri) : ProfileIntent
}

sealed interface ProfileUiEffect {
    data object OpenGallery : ProfileUiEffect
    data class ShowToast(val message: String) : ProfileUiEffect
}