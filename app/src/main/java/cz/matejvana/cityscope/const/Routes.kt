package cz.matejvana.cityscope.const

object Routes {
    const val CITY_SEARCH = "city_search"

    const val SETTINGS = "settings"

    fun getCityDetailRoute(cityId: String): String {
        return "cityDetail/$cityId"
    }

    const val MAP_SCREEN = "map_screen"

    const val FAVOURITE_CITIES = "favourite_cities"

    const val COUNTRY_SEARCH = "country_search"
    fun getCountryDetailRoute(countryId: String): String {
        return "country_detail/$countryId"
    }

    fun isRouteWithDisabledMenu(route: String?): Boolean {
        if (route == null) return false
        return isCityDetail(route) || isMap(route) || route.startsWith("country_detail/")
    }

    fun getMapRoute(cityName: String, latitude: String, longitude: String): String {
        return "$MAP_SCREEN/$cityName/$latitude/$longitude"
    }

    fun isCityDetail(route: String?): Boolean {
        return route?.startsWith("cityDetail/") == true
    }

    fun isMap(route: String?): Boolean {
        return route?.startsWith(MAP_SCREEN) == true
    }
}