package ru.geekbrains.applicationweatherv1.model

data class Weather(
    val city: City = getDefaultCity(),
    val temp: Int = 0,
    val feelsLike: Int = 0,
    val icon: String = "https://yastatic.net/weather/i/icons/blueye/color/svg/<значение из поля icon>.svg.",
    val windSpeed: Int = 0,
    val windDir: String = "s",
    val humidity: Int = 0,
    val tempMaxForecast: Int = 0,
    val feelsLikeForecast: Int = 0,
    val iconForecast: String = "https://yastatic.net/weather/i/icons/blueye/color/svg/<значение из поля icon>.svg."
)

fun getDefaultCity() = City("Нальчик", 43.4981, 43.6189)

