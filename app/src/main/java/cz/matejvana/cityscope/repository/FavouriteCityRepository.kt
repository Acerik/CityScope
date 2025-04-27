package cz.matejvana.cityscope.repository

import cz.matejvana.cityscope.data.FavouriteCity
import cz.matejvana.cityscope.data.FavouriteCity_
import io.objectbox.BoxStore

class FavouriteCityRepository(boxStore: BoxStore) {

    val favouriteCityBox = boxStore.boxFor(FavouriteCity::class.java)

    fun getAll(): List<FavouriteCity> {
        return favouriteCityBox.all
    }

    fun add(favouriteCity: FavouriteCity) {
        favouriteCityBox.put(favouriteCity)
    }

    fun remove(favouriteCity: FavouriteCity) {
        favouriteCityBox.remove(favouriteCity)
    }

    fun isCityFavourite(cityId: Long): Boolean {
        return favouriteCityBox.query().equal(FavouriteCity_.cityId, cityId).build().findFirst() != null
    }
}