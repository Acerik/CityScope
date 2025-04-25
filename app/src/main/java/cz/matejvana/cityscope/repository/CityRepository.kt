package cz.matejvana.cityscope.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.City_
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import java.io.InputStreamReader

class CityRepository(boxStore: BoxStore, private val context: Context) {

    private val cityBox: Box<City> = boxStore.boxFor(City::class.java)

    init {
        initializeData()
    }

    private fun initializeData() {
        cityBox.removeAll()
        if (cityBox.isEmpty) {
            val cities: List<City> = loadCitiesFromJson()
            println("Loaded ${cities.size} cities")
            cityBox.put(cities)
        }
    }

    private fun loadCitiesFromJson(): List<City> {
        val inputStream = context.assets.open("cities.json")
        val reader = InputStreamReader(inputStream)
        val cityListType = object : TypeToken<List<City>>() {}.type
        return Gson().fromJson(reader, cityListType)
    }

    fun getCityByName(name: String): City? {
        val query = cityBox.query(City_.name.equal(name.lowercase(), QueryBuilder.StringOrder.CASE_INSENSITIVE)).build()
        var city = query.findFirst()
        query.close()
        if (city == null) {
            val aliasQuery = cityBox.query(
                City_.aliases.containsElement(
                    name.lowercase(),
                    QueryBuilder.StringOrder.CASE_INSENSITIVE
                )
            ).build()
            city = aliasQuery.findFirst()
            aliasQuery.close()
        }
        return city
    }

    fun getCitiesByName(name: String): List<City> {
        val nameQuery =
            cityBox.query(City_.name.contains(name.lowercase(), QueryBuilder.StringOrder.CASE_INSENSITIVE)).build()
        val aliasQuery =
            cityBox.query(City_.aliases.contains(name.lowercase(), QueryBuilder.StringOrder.CASE_INSENSITIVE)).build()
        val cities = (nameQuery.find() + aliasQuery.find()).distinctBy { it.entityId }

        nameQuery.close()
        aliasQuery.close()

        return cities
            .sortedBy { it.population }
            .reversed()
    }

    fun getCityById(id: Long): City? {
        return cityBox.get(id)
    }

}