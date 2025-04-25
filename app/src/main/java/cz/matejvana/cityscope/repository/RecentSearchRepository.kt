package cz.matejvana.cityscope.repository

import cz.matejvana.cityscope.data.RecentCity
import cz.matejvana.cityscope.data.RecentCity_
import io.objectbox.Box

class RecentSearchRepository(private val recentCityBox: Box<RecentCity>) {

    val maxNumberOfRecentSearches: Int = 5

    fun getRecentSearches(limit: Int = maxNumberOfRecentSearches): List<RecentCity> {
        return recentCityBox.query().build().find().take(limit).reversed()
    }

    fun addSearch(city: RecentCity) {
        // Odstranění existujícího záznamu se stejným cityId
        recentCityBox.query().equal(RecentCity_.cityId, city.cityId).build().remove()
        // Přidání nového záznamu
        recentCityBox.put(city)
        val allCities = recentCityBox.all
        if (allCities.size > maxNumberOfRecentSearches) {
            recentCityBox.remove(allCities.first().id)
        }
    }
}