package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.CurrencyInfo
import cz.matejvana.cityscope.data.RecentCity
import cz.matejvana.cityscope.repository.CityRepository
import cz.matejvana.cityscope.repository.CountryCurrencyRepository
import cz.matejvana.cityscope.repository.RecentSearchRepository

class CityViewModel(
    private val cityRepository: CityRepository,
    private val countryCurrencyRepository: CountryCurrencyRepository,
    private val recentSearchRepository: RecentSearchRepository
) : ViewModel() {

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

    fun getRecentSearches(): List<City> {
        return cityRepository.getCitiesByIds(recentSearchRepository.getRecentSearches().map { it.cityId }.toList())
    }

    fun addRecentSearch(city: City) {
        recentSearchRepository.addSearch(RecentCity(cityId = city.entityId))
    }
}