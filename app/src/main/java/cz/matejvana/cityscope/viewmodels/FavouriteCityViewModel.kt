package cz.matejvana.cityscope.viewmodels

import androidx.lifecycle.ViewModel
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.FavouriteCity
import cz.matejvana.cityscope.repository.CityRepository
import cz.matejvana.cityscope.repository.FavouriteCityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavouriteCityViewModel(
    private val favouriteCityRepository: FavouriteCityRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _favouriteCities = MutableStateFlow<List<Long>>(emptyList())
    val favouriteCities: StateFlow<List<Long>> = _favouriteCities

    init {
        loadFavouriteCities()
    }

    private fun loadFavouriteCities() {
        _favouriteCities.value = favouriteCityRepository.getAll().map { it.cityId }
    }

    fun addFavouriteCity(cityId: Long) {
        favouriteCityRepository.add(favouriteCity = FavouriteCity(cityId = cityId))
        loadFavouriteCities()
    }

    fun removeFavouriteCity(cityId: Long) {
        val favouriteCity = favouriteCityRepository.getAll().find { it.cityId == cityId }
        if (favouriteCity != null) {
            favouriteCityRepository.remove(favouriteCity)
            loadFavouriteCities()
        }
    }

    fun getAllFavouriteCities(): List<City> {
        return cityRepository.getCitiesByIds(_favouriteCities.value)
    }
}