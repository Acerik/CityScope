package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.repository.CurrencyRate
import cz.matejvana.cityscope.viewmodels.CityViewModel
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CityDetailScreen(
    navController: NavController,
    cityId: Long,
    cityViewModel: CityViewModel = koinViewModel(),
    currencyExchangeViewModel: CurrencyExchangeViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    //todo implement detail screen
    // implementation should have all city infromation
    // add currency exchange
    // add weather
    // add map
    val city by remember { mutableStateOf(cityViewModel.getCityById(cityId)) }
    val countryCurrency by remember { mutableStateOf(cityViewModel.getCurrencyByCity(city!!)) }
    val currencyExchange by currencyExchangeViewModel.currencyRates.collectAsState()
    val preferredCurrencyCode by settingsViewModel.preferredCurrencyCode.collectAsState()

    LaunchedEffect(preferredCurrencyCode) {
        currencyExchangeViewModel.fetchCurrencyRates(preferredCurrencyCode ?: "eur")
    }

    if (city != null) {
        val currencyCode = countryCurrency.currencies.firstOrNull()?.code ?: "eur"
        val exchangeRate = when (currencyExchange) {
            is ApiResult.Success -> {
                (currencyExchange as ApiResult.Success<List<CurrencyRate>>).data
                    .find { it.code.equals(currencyCode, ignoreCase = true) }?.value
            }

            else -> null
        }
        val currentCurrencyInfo = settingsViewModel.getCurrencyInfoByCode(preferredCurrencyCode ?: "eur")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "City Name: ${city?.name}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Country: ${city?.country}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Population: ${city?.population}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Currency: ${countryCurrency.currencies.joinToString(", ") { it.name ?: "Unknown" }}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            when (currencyExchange) {
                is ApiResult.Loading -> {
                    Text(
                        text = "Exchange Rate: Loading...",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                is ApiResult.Success -> {
                    Text(
                        text = "Exchange Rate: 1${currentCurrencyInfo.symbol}(${currentCurrencyInfo.code}) = " +
                                "${String.format("%.2f", exchangeRate)}" +
                                "${countryCurrency.currencies.first().symbol}($currencyCode)",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                is ApiResult.Error -> {
                    Text(
                        text = "Exchange Rate: Error loading data",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    } else {
        Text(
            text = "City not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}