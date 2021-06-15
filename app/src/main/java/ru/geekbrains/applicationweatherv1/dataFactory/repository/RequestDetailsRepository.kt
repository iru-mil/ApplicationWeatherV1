package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.RemoteDataSource
import ru.geekbrains.applicationweatherv1.dataFactory.WeatherDTO

class RequestDetailsRepository(private val remoteDataSource: RemoteDataSource) :
    FavoritesDetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}