package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password).onSuccess { token ->
            userDataRepository.setAuthToken(token)
        }.map { Unit }
    }
}
