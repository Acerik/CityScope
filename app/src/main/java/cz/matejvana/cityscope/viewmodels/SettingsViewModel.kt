package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.const.DefaultValues
import cz.matejvana.cityscope.data.CurrencyInfo
import cz.matejvana.cityscope.repository.CountryCurrencyRepository
import cz.matejvana.cityscope.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val countryCurrencyRepository: CountryCurrencyRepository
) : ViewModel() {

    private val _preferredCurrencyCode =
        MutableStateFlow<String>(DefaultValues.DEFAULT_CURRENCY_CODE)
    val preferredCurrency: StateFlow<String> = _preferredCurrencyCode

    init {
        loadPreferredCurrencyCode()
    }

    private fun loadPreferredCurrencyCode() {
        _preferredCurrencyCode.value = settingsRepository.getPreferredCurrencyCode()
    }

    fun savePreferredCurrencyCode(currencyCode: String) {
        settingsRepository.savePreferredCurrencyCode(currencyCode)
        loadPreferredCurrencyCode()
    }

    fun getAllCurrencyCodes() = countryCurrencyRepository.getAllCurrencyCodes()

    fun getCurrencyInfoByCode(currencyCode: String): CurrencyInfo {
        return countryCurrencyRepository.getCurrencyInfoByCode(currencyCode)
    }
}