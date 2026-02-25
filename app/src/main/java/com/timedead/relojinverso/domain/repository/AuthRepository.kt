package com.timedead.relojinverso.domain.repository

import com.timedead.relojinverso.domain.model.User
import com.timedead.relojinverso.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Interface del repositorio de autenticación
 */
interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String, profile: UserProfile): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): User?
    fun isUserAuthenticated(): Flow<Boolean>
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
    suspend fun getUserProfile(): Result<UserProfile>
}
