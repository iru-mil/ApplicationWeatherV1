package ru.geekbrains.applicationweatherv1.dataFactory

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
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