package ru.geekbrains.applicationweatherv1.dataFactory

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val wind_speed: Int?,
    val wind_dir: String?,
    val humidity: Int?,
    val icon: String?
)