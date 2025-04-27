package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.const.DefaultValues
import cz.matejvana.cityscope.repository.CurrencyExchangeRepository
import cz.matejvana.cityscope.repository.CurrencyRate
import cz.matejvana.cityscope.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class CurrencyExchangeViewModel(
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _currencyRates = MutableStateFlow<ApiResult<List<CurrencyRate>>>(ApiResult.Loading)
    val currencyRates: StateFlow<ApiResult<List<CurrencyRate>>> = _currencyRates.asStateFlow()

    private val _preferredCurrencyCode = MutableStateFlow<String>(DefaultValues.DEFAULT_CURRENCY_CODE)
    val preferredCurrency: StateFlow<String> = _preferredCurrencyCode.asStateFlow()

    init {
        fetchCurrencyRates()
        loadPreferredCurrencyCode()
    }

    private fun loadPreferredCurrencyCode() {
        _preferredCurrencyCode.value = settingsRepository.getPreferredCurrencyCode()
    }

    private fun fetchCurrencyRates() {
        loadPreferredCurrencyCode()
        viewModelScope.launch {
            println("Fetching currency rates... INIT currency ${preferredCurrency.value}")
            _currencyRates.value = currencyExchangeRepository.getCurrencyRates(
                preferredCurrency.value.lowercase(Locale.getDefault())
            )
        }
    }

    fun fetchCurrencyRates(preferredCurrency: String) {
        viewModelScope.launch {
            println("Fetching currency rates... MANUAL currency $preferredCurrency")
            _currencyRates.value = ApiResult.Loading
            _currencyRates.value =
                currencyExchangeRepository.getCurrencyRates(preferredCurrency.lowercase(Locale.getDefault()))
        }
    }
}