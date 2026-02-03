package com.timedead.relojinverso.data.state

import com.timedead.relojinverso.domain.model.User

/**
 * Estados del módulo de autenticación siguiendo el patrón MVI
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object NotAuthenticated : AuthState()
}
