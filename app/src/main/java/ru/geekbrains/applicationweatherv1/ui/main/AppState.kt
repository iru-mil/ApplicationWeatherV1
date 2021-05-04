package ru.geekbrains.applicationweatherv1.ui.main

import ru.geekbrains.applicationweatherv1.model.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}