package com.timedead.relojinverso.domain.model

import com.google.gson.annotations.SerializedName

data class CountryLifeExpectancy(
    val Rank: Int,
    val Country: String,
    @SerializedName("Life Expectancy (both sexes)")
    val lifeExpectancyBoth: Double,
    @SerializedName("Females Life Expectancy")
    val femaleLifeExpectancy: Double,
    @SerializedName("Males Life Expectancy")
    val maleLifeExpectancy: Double
)
