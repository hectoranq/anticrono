package com.timedead.relojinverso.domain.model

/**
 * Sealed class que define todas las rutas de navegación de la aplicación
 * siguiendo el patrón Type-Safe Navigation
 */
sealed class Route(val route: String) {
    // Autenticación
    object SignIn : Route("sign_in")
    object Register : Route("register")
    object ForgotPassword : Route("forgot_password")
    
    // Death Timer (Home Principal)
    object DeathTimerHome : Route("death_timer_home")
    
    // Configuración
    object Settings : Route("settings")
}
