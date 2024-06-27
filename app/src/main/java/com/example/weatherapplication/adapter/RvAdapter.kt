package com.example.weatherapplication.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapplication.data.forecast.ForecastData
import com.example.weatherapplication.databinding.ItemLayoutBinding
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RvAdapter(private val forecastArray:ArrayList<ForecastData>): RecyclerView.Adapter<RvAdapter.ViewHolder>() {

  class ViewHolder(val binding:ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

  }
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
  }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder:ViewHolder, position:Int)
    {
      val currentItem=forecastArray[position]
      holder.binding.apply{
        val imageIcon=currentItem.weather[0].icon
        val imageURL= "https://openweathermap.org/img/wn/$imageIcon.png"
        Picasso.get().load(imageURL).into(imgItem)
        tvItemTemp.text="${currentItem.main.temp} C"
        tvStatus.text= currentItem.weather[0].description
        tvItemTime.text=displayTime(currentItem.dt_txt)
      }
        return
    }
  @RequiresApi(Build.VERSION_CODES.O)
  private fun displayTime(dtText: String): CharSequence? {
    val input=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val output=DateTimeFormatter.ofPattern("MM-dd HH:mm")
    val dateTime=LocalDateTime.parse(dtText,input)
    return output.format(dateTime)
  }

  override fun getItemCount(): Int {
    return forecastArray.size
  }
}