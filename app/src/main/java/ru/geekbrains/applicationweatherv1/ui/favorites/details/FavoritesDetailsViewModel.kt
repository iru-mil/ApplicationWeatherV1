package ru.geekbrains.applicationweatherv1.ui.favorites.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.applicationweatherv1.dataFactory.*
import ru.geekbrains.applicationweatherv1.dataFactory.app.App.Companion.getHistoryDao
import ru.geekbrains.applicationweatherv1.dataFactory.repository.LocalRepository
import ru.geekbrains.applicationweatherv1.dataFactory.repository.RequestLocalRepository
import ru.geekbrains.applicationweatherv1.dataFactory.app.AppState
import ru.geekbrains.applicationweatherv1.dataFactory.repository.FavoritesDetailsRepository
import ru.geekbrains.applicationweatherv1.dataFactory.repository.RequestDetailsRepository

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class FavoritesDetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val requestDetailsRepository: FavoritesDetailsRepository =
        RequestDetailsRepository(RemoteDataSource()),
    private val historyRepository: LocalRepository =
        RequestLocalRepository(getHistoryDao())
) : ViewModel() {

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        requestDetailsRepository.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    fun saveCityToDB(weather: Weather) {
        historyRepository.saveEntity(weather)
    }

    private val callBack = object :
        Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }

        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact == null || fact.temp == null || fact.feels_like == null || fact.wind_speed == null ||
                fact.humidity == null || fact.wind_dir.isNullOrEmpty() || fact.icon.isNullOrEmpty()
            ) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {

                AppState.Success(convertDtoToModel(serverResponse))
            }
        }
    }
}