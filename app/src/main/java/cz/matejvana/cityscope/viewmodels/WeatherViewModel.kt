package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.api.WeatherResponse
import cz.matejvana.cityscope.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weather = MutableStateFlow<ApiResult<WeatherResponse>>(ApiResult.Loading)
    val weather: StateFlow<ApiResult<WeatherResponse>> = _weather.asStateFlow()

    fun getWeather(cityName: String) {
        viewModelScope.launch {
            _weather.value = ApiResult.Loading
            _weather.value = weatherRepository.getWeather(cityName)
        }
    }
}