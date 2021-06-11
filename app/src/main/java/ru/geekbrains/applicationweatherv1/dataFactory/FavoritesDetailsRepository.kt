package ru.geekbrains.applicationweatherv1.dataFactory

interface FavoritesDetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}