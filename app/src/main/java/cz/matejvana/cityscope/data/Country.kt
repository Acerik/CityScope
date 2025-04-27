package cz.matejvana.cityscope.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class Country(

    @Id
    var id: Long = 0,
    val nameOfficial: String,
    val nameCommon: String,
    val countryCode: String,
    val cioc: String?,
    val tld: List<String>,
    val capital: List<String>,
    @Convert(converter = CurrencyListConverter::class, dbType = String::class)
    val currencies: List<CurrencyInfo>,
    val region: String,
    val subregion: String,
    val latitude: Double,
    val longitude: Double,
    var borders: List<String> = emptyList(),
    @Convert(converter = TranslationMapConverter::class, dbType = String::class)
    val translations: Map<String, Translation>,
    val area: Double,
    val population: Int,
    val carSide: String
) {
    fun getCommonNameByLocale(locale: String): String {
        return translations[locale]?.common ?: nameCommon
    }

    fun getOfficialNameByLocale(locale: String): String {
        return translations[locale]?.official ?: nameOfficial
    }
}

data class Translation(
    val official: String,
    val common: String
)


class TranslationMapConverter : PropertyConverter<Map<String, Translation>, String> {
    private val gson = Gson()

    override fun convertToDatabaseValue(entityProperty: Map<String, Translation>?): String {
        return gson.toJson(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): Map<String, Translation> {
        return if (databaseValue.isNullOrEmpty()) {
            emptyMap()
        } else {
            val type = object : TypeToken<Map<String, Translation>>() {}.type
            gson.fromJson(databaseValue, type)
        }
    }
}