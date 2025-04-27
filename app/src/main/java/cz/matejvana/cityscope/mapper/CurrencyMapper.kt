package cz.matejvana.cityscope.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.CurrencyInfo

object CurrencyMapper {

    @Composable
    fun mapCurrencyInfoForDisplay(countryCurrency: CountryCurrency): String {
        return mapCurrencyInfoListForDisplay(countryCurrency.currencies)
    }

    @Composable
    fun mapCurrencyInfoListForDisplay(currencies: List<CurrencyInfo>): String {
        return stringResource(
            R.string.city_detail_currency,
            "${currencies.joinToString(", ") { it.name + "(${it.symbol})" ?: "Unknown" }}"
        )
    }
}