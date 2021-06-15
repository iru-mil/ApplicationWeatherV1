package ru.geekbrains.applicationweatherv1.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.applicationweatherv1.dataFactory.app.App.Companion.getHistoryDao
import ru.geekbrains.applicationweatherv1.dataFactory.app.AppState
import ru.geekbrains.applicationweatherv1.dataFactory.repository.LocalRepository
import ru.geekbrains.applicationweatherv1.dataFactory.repository.RequestLocalRepository
import ru.geekbrains.applicationweatherv1.ui.favorites.FavoritesViewModel

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository =
        RequestLocalRepository(getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.postValue(AppState.Loading)
        Thread.sleep(FavoritesViewModel.TIME_TO_SLEEP)
        historyLiveData.postValue(AppState.Success(historyRepository.getAllHistory()))
    }
}
