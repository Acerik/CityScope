package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.repository.CurrencyExchangeRepository
import cz.matejvana.cityscope.repository.CurrencyRate
import cz.matejvana.cityscope.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class CurrencyExchangeViewModel(
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _currencyRates = MutableStateFlow<ApiResult<List<CurrencyRate>>>(ApiResult.Loading)
    val currencyRates: StateFlow<ApiResult<List<CurrencyRate>>> = _currencyRates.asStateFlow()

    init {
        fetchCurrencyRates()
    }

    private fun fetchCurrencyRates() {
        viewModelScope.launch {
            _currencyRates.value = currencyExchangeRepository.getCurrencyRates(
                settingsRepository.getPreferredCurrencyCode().first().toString().lowercase(Locale.getDefault())
            )
        }
    }

    fun fetchCurrencyRates(preferredCurrency: String) {
        viewModelScope.launch {
            _currencyRates.value = ApiResult.Loading
            _currencyRates.value =
                currencyExchangeRepository.getCurrencyRates(preferredCurrency.lowercase(Locale.getDefault()))
        }
    }
}