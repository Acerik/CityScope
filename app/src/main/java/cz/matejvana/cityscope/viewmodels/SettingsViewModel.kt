package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.matejvana.cityscope.data.CurrencyInfo
import cz.matejvana.cityscope.repository.CountryCurrencyRepository
import cz.matejvana.cityscope.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val countryCurrencyRepository: CountryCurrencyRepository
) : ViewModel() {
    val preferredCurrencyCode: StateFlow<String?> = settingsRepository.getPreferredCurrencyCode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun savePreferredCurrencyCode(currencyCode: String) {
        viewModelScope.launch {
            settingsRepository.savePreferredCurrencyCode(currencyCode)
        }
    }

    fun getAllCurrencyCodes() = countryCurrencyRepository.getAllCurrencyCodes()

    fun getCurrencyInfoByCode(currencyCode: String): CurrencyInfo {
        return countryCurrencyRepository.getCurrencyInfoByCode(currencyCode)
    }
}