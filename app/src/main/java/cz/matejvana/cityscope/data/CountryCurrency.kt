package cz.matejvana.cityscope.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class CountryCurrency(
    @Id
    var id: Long = 0,

    var countryCode: String = "",

    @Convert(converter = CurrencyListConverter::class, dbType = String::class)
    var currencies: List<CurrencyInfo> = emptyList()
)

data class CurrencyInfo(
    val code: String?,
    val name: String?,
    val symbol: String?
)

class CurrencyListConverter : PropertyConverter<List<CurrencyInfo>, String> {
    private val gson = Gson()
    private val type = object : TypeToken<List<CurrencyInfo>>() {}.type

    override fun convertToEntityProperty(databaseValue: String?): List<CurrencyInfo> {
        return if (databaseValue == null) emptyList()
        else gson.fromJson(databaseValue, type)
    }

    override fun convertToDatabaseValue(entityProperty: List<CurrencyInfo>?): String {
        return gson.toJson(entityProperty)
    }
}