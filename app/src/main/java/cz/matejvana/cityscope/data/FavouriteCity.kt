package cz.matejvana.cityscope.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class FavouriteCity(

    @Id
    var id: Long = 0,
    var cityId: Long = 0,
    var timestamp: Long = System.currentTimeMillis()
)