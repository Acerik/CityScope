package cz.matejvana.cityscope.data

import cz.matejvana.cityscope.const.DefaultValues
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class PreferredCurrency(

    @Id
    var id: Long = 0,
    var currencyCode: String = DefaultValues.DEFAULT_CURRENCY_CODE,
)
