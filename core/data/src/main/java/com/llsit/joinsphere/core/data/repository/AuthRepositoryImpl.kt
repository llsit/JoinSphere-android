package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.domain.repository.AuthRepository

class AuthRepositoryImpl(

) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }
}