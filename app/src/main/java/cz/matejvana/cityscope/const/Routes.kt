package cz.matejvana.cityscope.const

object Routes {
    const val CITY_SEARCH = "citySearch"

    const val SETTINGS = "settings"

    fun getCityDetailRoute(cityId: String): String {
        return "cityDetail/$cityId"
    }

    const val MAP_SCREEN = "map_screen"

    fun isRouteWithDisabledMenu(route: String?): Boolean {
        if (route == null) return false
        return isCityDetail(route) || isMap(route)
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