package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.WeatherDTO

interface FavoritesDetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}