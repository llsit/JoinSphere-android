package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password).map { }
    }
}
