package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout().onSuccess {
            userDataRepository.setAuthToken(null)
        }
    }
}
