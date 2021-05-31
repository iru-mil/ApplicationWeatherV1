package ru.geekbrains.applicationweatherv1.dataFactory

class RequestWeatherRepository : WeatherRepository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getFavoritesRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getFavoritesWorldCities()
    }
}