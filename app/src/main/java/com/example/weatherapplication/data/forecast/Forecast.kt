package com.example.weatherapplication.data.forecast

data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastData>,
    val message: Int
)