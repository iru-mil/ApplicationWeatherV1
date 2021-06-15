package ru.geekbrains.applicationweatherv1.dataFactory.repository

import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.dataFactory.convertHistoryEntityToWeather
import ru.geekbrains.applicationweatherv1.dataFactory.convertWeatherToEntity
import ru.geekbrains.applicationweatherv1.dataFactory.room.HistoryDao

class RequestLocalRepository(private val localDataSource: HistoryDao) : LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}