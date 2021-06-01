package ru.geekbrains.applicationweatherv1.dataFactory

class RequestWeatherRepository : WeatherRepository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorage() = Weather()

    override fun getWeatherFromLocalStorageRus() = getFavoritesRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getFavoritesWorldCities()

}