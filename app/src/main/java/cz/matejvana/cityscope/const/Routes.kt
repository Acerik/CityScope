package cz.matejvana.cityscope.const

object Routes {
    const val CITY_SEARCH = "citySearch"

    const val SETTINGS = "settings"

    fun getCityDetailRoute(cityId: String): String {
        return "cityDetail/$cityId"
    }

    const val MAP_SCREEN = "map_screen"

    fun getMapRoute(cityName: String, latitude: String, longitude: String): String {
        return "$MAP_SCREEN/$cityName/$latitude/$longitude"
    }
}