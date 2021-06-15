package ru.geekbrains.applicationweatherv1.dataFactory

import ru.geekbrains.applicationweatherv1.dataFactory.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    if (weatherDTO.fact == null) {
        throw Exception("Error")
    }
    val fact: FactDTO = weatherDTO.fact
    return listOf(
        Weather(
            getDefaultCity(),
            fact.temp!!,
            fact.feels_like!!,
            fact.icon!!,
            fact.wind_speed!!,
            fact.wind_dir!!,
            fact.humidity!!
        )
    )
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>):
        List<Weather> {
    return entityList.map {
        Weather(
            City(it.city, 0.0, 0.0),
            it.temp,
            it.feelsLike,
            it.icon,
            it.windSpeed,
            it.windDir,
            it.humidity
        )
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(
        0,
        weather.city.city,
        weather.temp,
        weather.feelsLike,
        weather.icon,
        weather.windSpeed,
        weather.windDir,
        weather.humidity
    )
}
