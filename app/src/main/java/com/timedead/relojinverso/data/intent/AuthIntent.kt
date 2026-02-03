package com.timedead.relojinverso.data.intent

/**
 * Intents (acciones del usuario) para el módulo de autenticación
 */
sealed class AuthIntent {
    data class SignIn(val email: String, val password: String) : AuthIntent()
    data class Register(val name: String, val email: String, val password: String) : AuthIntent()
    object SignOut : AuthIntent()
    object CheckAuthStatus : AuthIntent()
}
