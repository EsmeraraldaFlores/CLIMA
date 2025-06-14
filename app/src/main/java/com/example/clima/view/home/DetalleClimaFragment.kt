package com.example.clima.view.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.clima.databinding.FragmentDetalleClimaBinding
import com.example.clima.view.home.viewModel.ClimaFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetalleClimaFragment : Fragment() {

    private val viewModel: ClimaFragmentViewModel by viewModels() // ✅ Corrección
    private var _binding: FragmentDetalleClimaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleClimaBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weatherInfo.observe(viewLifecycleOwner) { rrweather ->
            binding.textCiudad.text = weather.location.name
            binding.textTemp.text = "${weather.current.tempc} °C"
            binding.textSaludo.text = weather.current.condition.text

            val formattedTime = viewModel.formatLocalTime(weather.location.localtime)
            binding.textSemana.text = formattedTime

            binding.textNumWind.text =  "${weather.current.windkph} km/h"
            binding.textNumTemperatura.text = "${weather.current.feelslikec}°C"

            Glide.with(this)
                .load("https:${weather.current.condition.icon}")
                .into(binding.imgClima)

        }


        viewModel.fetchWeather("dee6bfa4fe7f459f97e15507252005")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}