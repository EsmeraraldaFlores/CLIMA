package com.example.clima.network

import com.example.clima.network.RealtimeAPI
import com.example.clima.repository.safeCall
import com.example.clima.repository.ResultWrapper
import com.example.clima.view.home.Weather
import retrofit2.HttpException
import javax.inject.Inject

class ClimaRepository @Inject constructor(
    private val api: RealtimeAPI
) {
    suspend fun getWeatherInfo(apiKey: String, location: String) = safeCall {
        api.getWeatherInfo(apiKey, location).let {
            it.body() ?: throw HttpException(it)
        }
    }
}
