package com.timedead.relojinverso.data.intent

import com.timedead.relojinverso.domain.model.CountryLifeExpectancy
import java.time.LocalDate
import java.time.LocalTime

/**
 * Intents para el módulo Death Timer
 */
sealed class TimerIntent {
    object StartTimer : TimerIntent()
    object StopTimer : TimerIntent()
    object UpdateTimer : TimerIntent()
    
    data class SaveConfiguration(
        val birthDate: LocalDate,
        val country: CountryLifeExpectancy,
        val sleepHours: Int,
        val productiveStartTime: LocalTime,
        val productiveEndTime: LocalTime
    ) : TimerIntent()
    
    object LoadConfiguration : TimerIntent()
}
