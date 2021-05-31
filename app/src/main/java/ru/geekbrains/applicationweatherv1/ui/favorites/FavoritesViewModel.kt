package ru.geekbrains.applicationweatherv1.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.applicationweatherv1.dataFactory.RequestWeatherRepository
import ru.geekbrains.applicationweatherv1.dataFactory.WeatherRepository
import ru.geekbrains.applicationweatherv1.ui.home.AppState

class FavoritesViewModel(

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val requestWeatherRepository: WeatherRepository = RequestWeatherRepository()
) :
    ViewModel() {

    companion object {
        const val TIME_TO_SLEEP: Long = 1000
    }

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    //fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            Thread.sleep(TIME_TO_SLEEP)
            liveDataToObserve.postValue(AppState.Success(if (isRussian) requestWeatherRepository.getWeatherFromLocalStorageRus() else requestWeatherRepository.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}