package cz.matejvana.cityscope.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.matejvana.cityscope.data.Country
import cz.matejvana.cityscope.getCurrentLocale
import io.objectbox.BoxStore
import java.io.InputStreamReader

class CountryRepository(boxStore: BoxStore, private val context: Context) {

    private val countryBox = boxStore.boxFor(Country::class.java)

    init {
        initializeData()
    }

    private fun initializeData() {
        countryBox.removeAll()
        if (countryBox.isEmpty) {
            val countries: List<Country> = loadCountriesFromJson()
            println("Loaded ${countries.size} countries")
            countryBox.put(countries)
        }
    }

    private fun loadCountriesFromJson(): List<Country> {
        val inputStream = context.assets.open("countries.json")
        val reader = InputStreamReader(inputStream)
        val countryListType = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(reader, countryListType)
    }

    fun getCountryById(id: Long): Country? {
        return countryBox.get(id)
    }

    fun getCountryIdByCode(code: String): Long? {
        val id = countryBox.all.firstOrNull { it.countryCode == code }?.id
        if (id != null) {
            return id
        }
        return countryBox.all.firstOrNull { it.cioc == code }?.id
    }

    fun getCountryByCode(code: String): Country? {
        return countryBox.all.firstOrNull { it.countryCode == code } ?: countryBox.all.firstOrNull { it.cioc == code }
    }

    fun getCountriesBySearch(query: String, locale: String = getCurrentLocale(context)): List<Country> {
        return countryBox.all.filter { country ->
            country.nameOfficial.contains(query, ignoreCase = true) ||
                    country.nameCommon.contains(query, ignoreCase = true) ||
                    country.countryCode.contains(query, ignoreCase = true) ||
                    country.cioc?.contains(query, ignoreCase = true) == true ||
                    country.translations[locale]?.official?.contains(query, ignoreCase = true) == true ||
                    country.translations[locale]?.common?.contains(query, ignoreCase = true) == true
        }.sortedBy { it.nameOfficial }
    }

}
