package com.example.clima.network

import com.example.clima.network.RealtimeAPI
import com.example.clima.repository.safeCall
import com.example.clima.repository.ResultWrapper
import com.example.clima.view.home.ForecastResponse
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: RealtimeAPI
) {
    suspend fun getForecast(apiKey: String, location: String) = safeCall {
        api.getForecastInfo(apiKey, location).run {
            if (isSuccessful) body() ?: throw Exception("Datos nulos")
            else throw HttpException(this)
        }
    }
}
