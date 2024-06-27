package com.example.weatherapplication.data.models

data class Main(var temp      : Double? = null,
                var feels_like : Double,
                var temp_min   : Double,
                var temp_max   : Double,
                var pressure  : Int?    = null,
                var humidity  : Int?    = null)
