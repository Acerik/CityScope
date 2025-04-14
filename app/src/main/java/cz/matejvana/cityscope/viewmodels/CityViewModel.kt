package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.CityRepository
import cz.matejvana.cityscope.data.City

class CityViewModel(private val cityRepository: CityRepository) : ViewModel() {

    fun searchCityByName(name: String): City? {
        return cityRepository.getCityByName(name)
    }

    fun searchCitiesByName(name: String): List<City> {
        return cityRepository.getCitiesByName(name)
    }
}