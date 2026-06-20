package com.llsit.joinsphere.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.model.UserProfileDto
import io.github.jan.supabase.postgrest.from

class AuthRepositoryImpl(
    private val supabase: SupabaseClient
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<String> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val userId = supabase.auth.currentUserOrNull()?.id
        userId ?: throw Exception("User not found")
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String> = runCatching {
        val authResult = supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        val userId = authResult?.id
        userId ?: throw Exception("User not found")

        val initialProfile = UserProfileDto(
            id = userId,
            name = name,
            email = email
        )

        supabase.from("users").insert(initialProfile)

        userId
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }
}
