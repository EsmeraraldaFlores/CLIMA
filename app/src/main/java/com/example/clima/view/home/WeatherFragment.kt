package com.example.clima.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.clima.core.LocationProvider
import com.example.clima.databinding.FragmentWeatherBinding
import com.example.clima.view.home.adapters.ForecastDayAdapter
import com.example.clima.view.home.viewModel.WeatherFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherFragmentViewModel by viewModels()
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var forecastAdapter: ForecastDayAdapter
    private val locationProvider by lazy { LocationProvider(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastAdapter = ForecastDayAdapter(emptyList())
        binding.recyclerViewWeather.apply {
            adapter = forecastAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        locationProvider.getLastLocation { location ->
            val loc = location?.let { "${it.latitude},${it.longitude}" } ?: "19.4326,-99.1332"
            viewModel.fetchForecast("dee6bfa4fe7f459f97e15507252005", loc)
        }

        viewModel.forecastInfo.observe(viewLifecycleOwner) { forecastResponse ->
            val today = forecastResponse.current

            binding.textTemp.text = "${today.tempc} °C"
            binding.textNumWind.text = "${today.windkph} km/h"
            binding.textNumTemperatura.text = "${today.feelslikec}°C"
            Glide.with(this)
                .load("https:${today.condition.icon}")
                .into(binding.imgClima)

            binding.textSaludo.text = "${getGreeting()} WASIM"
            binding.textSemana.text = formatLastUpdated(today.lastupdated)
            forecastAdapter.updateData(forecastResponse.forecast.forecastday)
        }
    }

    private fun getGreeting(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "BUENOS DÍAS"
            in 12..17 -> "BUENAS TARDES"
            else -> "BUENAS NOCHES"
        }
    }

    private fun formatLastUpdated(raw: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, hh:mm a", Locale.getDefault())
        return inputFormat.parse(raw)?.let {
            outputFormat.format(it).uppercase(Locale.getDefault())
        } ?: raw
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
