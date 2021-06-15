package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}