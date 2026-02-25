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
    
    // Navegación Principal (Bottom Navigation)
    object Inicio : Route("inicio")
    object Fin : Route("fin")
    object Estadisticas : Route("estadisticas")
    object ScreenTime : Route("screen_time")
    object Valor : Route("valor")
    object Perfil : Route("perfil")
    
    // Death Timer (Home Principal - Alias de Inicio)
    object DeathTimerHome : Route("inicio")
    
    // Configuración
    object Settings : Route("settings")
}
