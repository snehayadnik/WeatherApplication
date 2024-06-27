package com.example.weatherapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.adapter.RvAdapter
import com.example.weatherapplication.data.forecast.ForecastData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.example.weatherapplication.databinding.BottomsheetdialogBinding
import com.example.weatherapplication.retrofit.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetLayoutBinding: BottomsheetdialogBinding
    private lateinit var dialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sheetLayoutBinding=BottomsheetdialogBinding.inflate(layoutInflater)
        dialog= BottomSheetDialog(this, R.style.BottomSheetTheme)
        dialog.setContentView(sheetLayoutBinding.root)
        setContentView(binding.root)
        getWeather()
        binding.buttonForecast.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        getCurrentForecast()

        sheetLayoutBinding.rvForecast.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 1, RecyclerView.HORIZONTAL, false)
        }
        dialog.window?.attributes?.windowAnimations=R.style.DialogAnimation
        dialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentForecast() {
            GlobalScope.launch(Dispatchers.IO)
            {
                val response = try {
                    RetrofitInstance.weatherInstance.getForecast(
                        "Pune",
                        "metric",
                        "54639c70d526766091bcb5ba4fad3768"
                    )
                } catch (e: IOException) {
                    Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                    return@launch
                } catch (e: HttpException) {
                    Toast.makeText(
                        applicationContext,
                        "http error  ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val data = response.body()!!
                        val forecastArray: ArrayList<ForecastData> = data.list as ArrayList<ForecastData>
                        val adapter = RvAdapter(forecastArray)
                        sheetLayoutBinding.rvForecast.adapter = adapter

                    }
                }
            }
        }


    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    private fun getWeather() {
        GlobalScope.launch(Dispatchers.IO)
        {
            val response = try {
                RetrofitInstance.weatherInstance.getCurrentWeather(
                    "Pune",
                    "metric",
                    "54639c70d526766091bcb5ba4fad3768"
                )
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error  ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data = response.body()!!
                    val iconId = data.weather[0].icon
                    val imgUrl = "https://openweathermap.org/img/wn/$iconId.png"
                    Picasso.get().load(imgUrl).into(binding.imgTemp)
                    binding.tvSunrise.text = dateFormatConverter(
                        data.sys.sunset.toLong()
                    )

                    binding.tvSunset.text = dateFormatConverter(
                        data.sys.sunset.toLong()
                    )



                    binding.apply {
                        cloudy.text = data.weather[0].description
                        tvWindSpeed.text = "${data.wind.speed.toString()}km/hr"
                        tvLocation.text = "${data.name}\n${data.sys.country}"
                        temp.text = "${data.main.temp} C"
                        tvFeelsLike.text = "Feels like: ${data.main.feels_like}"
                        maxTemp.text = "Max Temp: ${data.main.temp_max} C"
                        minTemp.text = "Min Temp: ${data.main.temp_min} C"
                        tvHumidity.text = "${data.main.humidity} %"
                        tvPressure.text = "${data.main.pressure}hPa"
                        tvUpdate.text = "Last Update ${
                            SimpleDateFormat(
                                "hh:mm a",
                                Locale.ENGLISH
                            ).format(data.dt.times(1000))
                        }"
                        getCurrentAQI(data.coord.lat, data.coord.long)
                    }

                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrentAQI(lat:Double,long:Double) {
        GlobalScope.launch(Dispatchers.IO)
        {
            val response = try {
                RetrofitInstance.weatherInstance.getAQI(
                    18.51957,
                    73.85535,
                    "54639c70d526766091bcb5ba4fad3768"
                )
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error  ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data = response.body()!!
                    val num = data.list[0].main.aqi
                    binding.tvAirQuality.text=when(num)
                    {
                        1->"Good"
                        2->"Fair"
                        3->"Moderate"
                        4->"Poor"
                        5->"Very Poor"
                        else->"No Data "
                    }
                }
            }
        }
    }

    private fun dateFormatConverter(date: Long): String {

        return SimpleDateFormat(
            "hh:mma",
            Locale.ENGLISH
        ).format(Date(date * 1000))
    }
}

