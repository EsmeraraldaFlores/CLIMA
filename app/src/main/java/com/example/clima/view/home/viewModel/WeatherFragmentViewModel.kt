package com.example.clima.view.home.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.core.LocationProvider
import com.example.clima.core.ResultWrapper
import com.example.clima.model.ForecastResponse
import com.example.clima.model.Weather
import com.example.clima.network.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider
): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loaderState

    private val _forecastInfo = MutableLiveData<ForecastResponse>()
    val forecastInfo: LiveData<ForecastResponse> get() = _forecastInfo


    fun fetchForecast(apiKey: String) {
        _loaderState.value = true
        viewModelScope.launch {

            val location = "19.4326,-99.1332"
            when (val result = repository.getForecast(apiKey, location)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _forecastInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false

                }
            }
        }
    }


}