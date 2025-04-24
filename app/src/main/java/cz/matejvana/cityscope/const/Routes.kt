package cz.matejvana.cityscope.const

object Routes {
    const val CITY_SEARCH = "citySearch"

    const val SETTINGS = "settings"

    fun getCityDetailRoute(cityId: String): String {
        return "cityDetail/$cityId"
    }
}