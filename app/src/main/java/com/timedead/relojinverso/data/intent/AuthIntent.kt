package com.timedead.relojinverso.data.intent

import com.timedead.relojinverso.domain.model.UserProfile

/**
 * Intents (acciones del usuario) para el módulo de autenticación
 */
sealed class AuthIntent {
    data class SignIn(val email: String, val password: String) : AuthIntent()
    data class Register(
        val name: String, 
        val email: String, 
        val password: String,
        val profile: UserProfile
    ) : AuthIntent()
    object SignOut : AuthIntent()
    object CheckAuthStatus : AuthIntent()
}
