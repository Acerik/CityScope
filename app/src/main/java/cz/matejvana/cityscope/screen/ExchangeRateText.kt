package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.repository.CurrencyRate
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExchangeRateText(
    countryCurrency: CountryCurrency,
    currencyExchangeViewModel: CurrencyExchangeViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val currencyExchange by currencyExchangeViewModel.currencyRates.collectAsState()
    val preferredCurrencyCode by settingsViewModel.preferredCurrency.collectAsState()

    val currencyCode = countryCurrency.currencies.firstOrNull()?.code ?: "eur"
    LaunchedEffect(preferredCurrencyCode) {
        currencyExchangeViewModel.fetchCurrencyRates(preferredCurrencyCode)
    }

    val exchangeRate = when (currencyExchange) {
        is ApiResult.Success -> {
            (currencyExchange as ApiResult.Success<List<CurrencyRate>>).data
                .find { it.code.equals(currencyCode, ignoreCase = true) }?.value
        }

        else -> null
    }
    val currentCurrencyInfo = settingsViewModel.getCurrencyInfoByCode(preferredCurrencyCode)
    when (currencyExchange) {
        is ApiResult.Loading -> {
            Text(
                text = stringResource(R.string.city_detail_exchange_rate_loading),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        is ApiResult.Success -> {
            Text(
                text = stringResource(
                    R.string.city_detail_exchange_rate,
                    currentCurrencyInfo.symbol,
                    currentCurrencyInfo.code,
                    String.format("%.2f", exchangeRate),
                    countryCurrency.currencies.first().symbol,
                    currencyCode
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        is ApiResult.Error -> {
            Text(
                text = stringResource(R.string.city_detail_exchange_rate_error),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}