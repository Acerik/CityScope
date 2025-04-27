package cz.matejvana.cityscope.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.data.Country
import cz.matejvana.cityscope.repository.CountryRepository

class CountryViewModel(private val countryRepository: CountryRepository, private val context: Context) : ViewModel() {

    fun searchCountries(query: String): List<Country> {
        return countryRepository.getCountriesBySearch(query)
    }

    fun getCountryById(id: Long): Country? {
        return countryRepository.getCountryById(id)
    }

    fun getCountryIdByCode(code: String): Long? {
        return countryRepository.getCountryIdByCode(code)
    }

    fun getCountryByCode(code: String): Country? {
        return countryRepository.getCountryByCode(code)
    }

    fun getContext(): Context {
        return context
    }
}