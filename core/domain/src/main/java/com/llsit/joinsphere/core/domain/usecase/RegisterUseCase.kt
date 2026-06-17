package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return authRepository.register(name, email, password).onSuccess { token ->
            userDataRepository.setAuthToken(token)
        }.map { }
    }
}