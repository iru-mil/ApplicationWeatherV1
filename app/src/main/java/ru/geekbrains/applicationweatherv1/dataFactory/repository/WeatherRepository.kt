package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.Weather

interface WeatherRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}