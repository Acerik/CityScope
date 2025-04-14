package cz.matejvana.cityscope.mapper

import cz.matejvana.cityscope.data.City

object CityMapper {

    fun getDisplayName(city: City): String {
        return city.name + "(" + city.country + ")"
    }

}