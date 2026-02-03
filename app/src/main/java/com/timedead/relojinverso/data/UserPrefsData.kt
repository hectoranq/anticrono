package com.timedead.relojinverso.data

import com.timedead.relojinverso.domain.model.CountryLifeExpectancy
import java.time.LocalDate

data class UserPrefsData(
    val date: LocalDate,
    val country: CountryLifeExpectancy?,
    val sleepHours: Int,
    val productiveHours: Int
)
