package com.timedead.relojinverso.data

import java.time.LocalDate

/**
 * Datos calculados del Death Timer para compartir entre pantallas
 */
data class DeathTimerData(
    // Tiempo restante
    val timeLeft: TimeLeft,
    val diasRestantes: Long,
    
    // Información del usuario
    val edad: Int,
    val etapaDeVida: String,
    val porcentajeVidaVivida: Float,
    val porcentajeVidaRestante: Float,
    
    // Tiempo de pantalla
    val screenTimeMillis: Long,
    val screenTimeToday: Long,
    
    // Uso por categorías
    val ocioMillis: Long,
    val productividadMillis: Long,
    val ocioPorcentaje: Float,
    val productividadPorcentaje: Float,
    
    // Tiempo productivo restante
    val horasSueno: Int,
    val horasProductivas: Int,
    
    // Estado del corazón (intensidad)
    val heartDrawableId: Int,
    
    // Timestamp de última actualización
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * Datos de uso por categoría
 */
data class UsageByCategory(
    val ocioMillis: Long,
    val productividadMillis: Long,
    val ocioPorcentaje: Float,
    val productividadPorcentaje: Float
)
