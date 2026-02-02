package com.timedead.relojinverso.data

import com.timedead.relojinverso.CountryLifeExpectancy
import java.time.LocalDate

data class UserPrefsData(
    val date: LocalDate,
    val country: CountryLifeExpectancy?,
    val sleepHours: Int,
    val productiveHours: Int
)
