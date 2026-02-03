package com.timedead.relojinverso.domain.model

import java.time.LocalDate

/**
 * Modelo de dominio para el usuario
 */
data class User(
    val id: String,
    val email: String,
    val name: String,
    val birthDate: LocalDate? = null,
    val country: CountryLifeExpectancy? = null,
    val sleepHours: Int = 8,
    val productiveHours: Int = 8
)
