package com.llsit.joinsphere

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val preferencesDataSource: PreferencesDataSource
) : ViewModel() {

    fun checkOnboarding(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            delay(1500)
            val shouldShowOnboarding = preferencesDataSource.shouldShowOnboarding.first()
            onResult(shouldShowOnboarding)
        }
    }
}
