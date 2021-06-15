package ru.geekbrains.applicationweatherv1.dataFactory.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temp: Int,
    val feelsLike: Int,
    val icon: String,
    val windSpeed: Int,
    val windDir: String,
    val humidity: Int
)
