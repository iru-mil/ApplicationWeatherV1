package ru.geekbrains.applicationweatherv1.ui.home

import ru.geekbrains.applicationweatherv1.dataFactory.Weather

sealed class AppState {
    data class SuccessHome(val weatherData: Weather) : AppState()
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}