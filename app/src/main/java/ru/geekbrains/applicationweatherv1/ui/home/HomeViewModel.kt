package ru.geekbrains.applicationweatherv1.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.applicationweatherv1.dataFactory.repository.RequestWeatherRepository
import ru.geekbrains.applicationweatherv1.dataFactory.repository.WeatherRepository
import ru.geekbrains.applicationweatherv1.dataFactory.app.AppState
import ru.geekbrains.applicationweatherv1.ui.favorites.FavoritesViewModel

class HomeViewModel(

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val requestWeatherRepository: WeatherRepository = RequestWeatherRepository()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    //fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            Thread.sleep(FavoritesViewModel.TIME_TO_SLEEP)
            liveDataToObserve.postValue(AppState.SuccessHome(requestWeatherRepository.getWeatherFromLocalStorage()))
        }.start()
    }
}