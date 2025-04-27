package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.CurrencyInfo
import cz.matejvana.cityscope.data.RecentCity
import cz.matejvana.cityscope.repository.CityRepository
import cz.matejvana.cityscope.repository.CountryCurrencyRepository
import cz.matejvana.cityscope.repository.RecentSearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CityViewModel(
    private val cityRepository: CityRepository,
    private val countryCurrencyRepository: CountryCurrencyRepository,
    private val recentSearchRepository: RecentSearchRepository
) : ViewModel() {

    private val _recentSearches = MutableStateFlow<List<City>>(emptyList())
    val recentSearches: StateFlow<List<City>> = _recentSearches

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        _recentSearches.value =
            cityRepository.getCitiesByIds(recentSearchRepository.getRecentSearches().map { it.cityId }.toList())
    }

    fun searchCityByName(name: String): City? {
        return cityRepository.getCityByName(name)
    }

    fun searchCitiesByName(name: String): List<City> {
        return cityRepository.getCitiesByName(name)
    }

    fun getCityById(id: Long): City? {
        return cityRepository.getCityById(id)
    }

    fun getCurrencyByCity(city: City): CountryCurrency {
        return countryCurrencyRepository.getCurrencyByCountryCode(city.country) ?: CountryCurrency()
    }

    fun getAllCurrencyCodes(): List<CurrencyInfo> {
        return countryCurrencyRepository.getAllCurrencyCodes()
    }
    
    fun addRecentSearch(city: City) {
        recentSearchRepository.addSearch(RecentCity(cityId = city.entityId))
        loadRecentSearches()
    }
}