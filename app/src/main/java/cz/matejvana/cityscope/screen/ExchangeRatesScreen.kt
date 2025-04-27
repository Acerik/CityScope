package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.data.CurrencyInfo
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExchangeRatesScreen(
    navController: NavController,
    currencyExchangeViewModel: CurrencyExchangeViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val preferredCurrencyCode by settingsViewModel.preferredCurrency.collectAsState()
    val allCurrencyCodes = remember { settingsViewModel.getAllCurrencyCodes() }
    val currencyExchange by currencyExchangeViewModel.currencyRates.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember {
        mutableStateOf(allCurrencyCodes.find { it.code.lowercase() == preferredCurrencyCode.lowercase() }
            ?: allCurrencyCodes.first())
    }
    LaunchedEffect(selectedCurrency) {
        currencyExchangeViewModel.fetchCurrencyRates(selectedCurrency.code)
    }
    Column {

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (selectedCurrency.code.isNotEmpty()) selectedCurrency.code else stringResource(R.string.settings_select_currency))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allCurrencyCodes.forEach { currencyCode ->
                    DropdownMenuItem(
                        text = { Text("${currencyCode.symbol} | ${currencyCode.name}") },
                        onClick = {
                            selectedCurrency = currencyCode
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        val currencyExchangeState = currencyExchange

        when (currencyExchangeState) {
            is ApiResult.Loading -> {
                Text(
                    text = stringResource(R.string.city_detail_exchange_rate_loading),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            is ApiResult.Success -> {
                Text(
                    text = stringResource(R.string.menu_exchange_rates),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                LazyColumn {
                    items(allCurrencyCodes) { currencyCode ->
                        val rate =
                            currencyExchangeState.data.find { it.code.lowercase() == currencyCode.code.lowercase() }
                        if (rate != null) {
                            ExchangeRateListItem(
                                selectedCurrency,
                                currencyCode,
                                rate.value
                            )
                        }
                    }
                }
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
}

@Composable
fun ExchangeRateListItem(
    fromCurrency: CurrencyInfo,
    toCurrency: CurrencyInfo,
    exchangeRate: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "1 ${fromCurrency.symbol} (${fromCurrency.code})",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = fromCurrency.name,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
        ) {
            Text(
                text = String.format("%.2f ${toCurrency.symbol} (${toCurrency.code})", exchangeRate),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = toCurrency.name,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}