package cz.matejvana.cityscope.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class RecentCity(
    @Id var id: Long = 0,
    val cityId: Long
)