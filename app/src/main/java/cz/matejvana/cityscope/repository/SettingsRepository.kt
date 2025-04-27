package cz.matejvana.cityscope.repository

import cz.matejvana.cityscope.const.DefaultValues
import cz.matejvana.cityscope.data.PreferredCurrency
import cz.matejvana.cityscope.data.PreferredCurrency_
import io.objectbox.BoxStore

class SettingsRepository(boxStore: BoxStore) {

    private val preferredCurrencyBox = boxStore.boxFor(PreferredCurrency::class.java)


    init {
        if (preferredCurrencyBox.isEmpty) {
            preferredCurrencyBox.put(
                PreferredCurrency(
                    currencyCode = DefaultValues.DEFAULT_CURRENCY_CODE
                )
            )
        }
    }

    fun getPreferredCurrencyCode(): String {
        val preferredCurrency = preferredCurrencyBox.query().orderDesc(PreferredCurrency_.id).build().findFirst()
        return preferredCurrency?.currencyCode ?: DefaultValues.DEFAULT_CURRENCY_CODE
    }

    fun savePreferredCurrencyCode(currencyCode: String) {
        println("save currency code $currencyCode")
        preferredCurrencyBox.put(
            PreferredCurrency(
                currencyCode = currencyCode
            )
        )
    }
}