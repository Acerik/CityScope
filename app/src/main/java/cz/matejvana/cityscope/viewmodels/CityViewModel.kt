package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.repository.CityRepository

class CityViewModel(private val cityRepository: CityRepository) : ViewModel() {

    fun searchCityByName(name: String): City? {
        return cityRepository.getCityByName(name)
    }

    fun searchCitiesByName(name: String): List<City> {
        //todo enchance this method to search by alias
        // seach by alias should be done when search is empty ???
        return cityRepository.getCitiesByName(name)
    }

    fun getCityById(id: Long): City? {
        return cityRepository.getCityById(id)
    }
}