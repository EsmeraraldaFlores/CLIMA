package com.example.proyectoshopifyka.network

import com.example.proyectoshopifyka.core.RealtimeAPI
import com.example.proyectoshopifyka.core.safeCall
import com.example.proyectoshopifyka.core.ResultWrapper
import com.example.proyectoshopifyka.model.ForecastResponse
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
