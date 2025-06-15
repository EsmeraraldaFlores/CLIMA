package com.example.proyectoshopifyka.network

import com.example.proyectoshopifyka.core.RealtimeAPI
import com.example.proyectoshopifyka.core.safeCall
import com.example.proyectoshopifyka.core.ResultWrapper
import com.example.proyectoshopifyka.model.Weather
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
