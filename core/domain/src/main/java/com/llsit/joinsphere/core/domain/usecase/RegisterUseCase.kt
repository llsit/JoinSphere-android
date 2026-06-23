package com.llsit.joinsphere.core.domain.usecase

import com.llsit.joinsphere.core.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return authRepository.register(name, email, password).map { }
    }
}