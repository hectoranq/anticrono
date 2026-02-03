package com.timedead.relojinverso.data.state

import com.timedead.relojinverso.data.TimeLeft
import com.timedead.relojinverso.domain.model.CountryLifeExpectancy
import java.time.LocalDate

/**
 * Estados del módulo Death Timer siguiendo el patrón MVI
 */
sealed class TimerState {
    object Idle : TimerState()
    object Loading : TimerState()
    
    data class TimerRunning(
        val timeLeft: TimeLeft,
        val birthDate: LocalDate,
        val country: CountryLifeExpectancy,
        val sleepHours: Int,
        val productiveHours: Int,
        val screenTimeMillis: Long,
        val daysRemaining: Long,
        val lifePercentage: Float
    ) : TimerState()
    
    object ConfigurationRequired : TimerState()
    data class Error(val message: String) : TimerState()
}
