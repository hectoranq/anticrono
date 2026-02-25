package com.timedead.relojinverso.domain.model

/**
 * Modelo de perfil de usuario para Firebase Realtime Database
 */
data class UserProfile(
    val fullName: String = "",
    val birthDate: String = "",       // yyyy-MM-dd
    val birthCountry: String = "",
    val sleepHours: Int = 8,
    val tprStart: String = "",        // HH:mm
    val tprEnd: String = ""           // HH:mm
)
