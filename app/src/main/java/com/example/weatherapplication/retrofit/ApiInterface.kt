package com.example.weatherapplication.retrofit
import com.example.weatherapplication.data.airquality.AQI
import com.example.weatherapplication.data.forecast.Forecast
import com.example.weatherapplication.data.models.CurrentWeather
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query
//city &weather in data clas then weather has separate dataclass

/*const val apiKey="54639c70d526766091bcb5ba4fad3768"*/
interface ApiInterface {
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") q: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): Response<CurrentWeather>

    @GET("forecast?")
    suspend fun getForecast(
        @Query("q") q: String,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String
    ): Response<Forecast>

    @GET("air_pollution?")
    suspend fun getAQI(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("apiKey") apiKey: String
    ): Response<AQI>

}


