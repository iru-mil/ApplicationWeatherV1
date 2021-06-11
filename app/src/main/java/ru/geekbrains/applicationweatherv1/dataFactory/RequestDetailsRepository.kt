package ru.geekbrains.applicationweatherv1.dataFactory

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