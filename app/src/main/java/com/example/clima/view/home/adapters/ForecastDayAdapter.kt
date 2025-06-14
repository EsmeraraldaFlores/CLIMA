package com.example.clima.model

data class ForecastDay(
    val date: String = "",
    val day: Day = Day()
)

data class Day(
    val avgtemp_c: Double = 0.0,
    val maxwind_kph: Double = 0.0,
    val condition: Condition = Condition()
)

data class Condition(
    val text: String = "",
    val icon: String = ""
)
