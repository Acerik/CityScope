package cz.matejvana.cityscope.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class City(
    @Id var entityId: Long = 0,
    val name: String,
    val aliases: List<String>, // Alias je textový řetězec, který může obsahovat více aliasů oddělených čárkami
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Int,
    val timezone: String
)
