package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.dataFactory.getFavoritesRussianCities
import ru.geekbrains.applicationweatherv1.dataFactory.getFavoritesWorldCities

class RequestWeatherRepository : WeatherRepository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorage() = Weather()

    override fun getWeatherFromLocalStorageRus() = getFavoritesRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getFavoritesWorldCities()

}