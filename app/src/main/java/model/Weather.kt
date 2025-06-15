package com.example.proyectoshopifyka.model

data class Weather(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String = "",
    val localtime: String = ""
)

data class Current(
    val temp_c: Double = 0.0,
    val condition: Condition = Condition(),
    val wind_kph: Double = 0.0,
    val feelslike_c: Double = 0.0,
    val last_updated: String = ""
)

data class Condition(
    val text: String = "",
    val icon: String = "",
    val code: Int = 0
)
