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
            _weather.value = ApiResult.Loading // Reset stavu
            try {
                val result = weatherRepository.getWeather(cityName)
                if (result is ApiResult.Success) {
                    _weather.value = ApiResult.Success(result.data)
                } else if (result is ApiResult.Error) {
                    _weather.value = ApiResult.Error(result.message)
                }
            } catch (e: Exception) {
                _weather.value = ApiResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}