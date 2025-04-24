package cz.matejvana.cityscope.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.CountryCurrency_
import io.objectbox.Box
import io.objectbox.BoxStore
import java.io.InputStreamReader

class CountryCurrencyRepository(boxStore: BoxStore, private val context: Context) {

    private val cityBox: Box<CountryCurrency> = boxStore.boxFor(CountryCurrency::class.java)

    init {
        initializeData()
    }

    private fun initializeData() {
        cityBox.removeAll()
        if (cityBox.isEmpty) {
            val currencies: List<CountryCurrency> = loadCountryCurrenciesFromJson()
            println("Loaded ${currencies.size} currencies")
            cityBox.put(currencies)
        }
    }

    private fun loadCountryCurrenciesFromJson(): List<CountryCurrency> {
        val inputStream = context.assets.open("country_currencies.json")
        val reader = InputStreamReader(inputStream)
        val countryCurrencies = object : TypeToken<List<CountryCurrency>>() {}.type
        return Gson().fromJson(reader, countryCurrencies)
    }

    fun getCurrencyByCountryCode(countryCode: String): CountryCurrency? {
        val query = cityBox.query(CountryCurrency_.countryCode.equal(countryCode)).build()
        val currency = query.findFirst()
        query.close()
        return currency
    }
}