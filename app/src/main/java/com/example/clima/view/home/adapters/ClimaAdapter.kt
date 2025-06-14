package com.example.clima.view.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clima.databinding.ItemWeatherBinding
import com.example.clima.model.Weather

class WeatherAdapter(private var weatherList: List<Weather>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Weather) {
            binding.txtCity.text = item.location.name
            binding.txtTemp.text = "${item.current.tempc} °C"
            binding.txtCondition.text = item.current.condition.text

            Glide.with(binding.root.context)
                .load("https:${item.current.condition.icon}")
                .into(binding.imgIcon)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size
}
