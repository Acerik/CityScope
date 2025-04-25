package cz.matejvana.cityscope.screen

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.const.Routes
import cz.matejvana.cityscope.repository.CurrencyRate
import cz.matejvana.cityscope.viewmodels.CityViewModel
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import cz.matejvana.cityscope.viewmodels.WeatherViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CityDetailScreen(
    navController: NavController,
    cityId: Long,
    cityViewModel: CityViewModel = koinViewModel(),
    currencyExchangeViewModel: CurrencyExchangeViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    //todo implement detail screen
    // implementation should have all city infromation

    val city by remember { mutableStateOf(cityViewModel.getCityById(cityId)) }
    val countryCurrency by remember { mutableStateOf(cityViewModel.getCurrencyByCity(city!!)) }
    val currencyExchange by currencyExchangeViewModel.currencyRates.collectAsState()
    val preferredCurrencyCode by settingsViewModel.preferredCurrencyCode.collectAsState()

    LaunchedEffect(preferredCurrencyCode, city) {
        currencyExchangeViewModel.fetchCurrencyRates(preferredCurrencyCode ?: "eur")
        weatherViewModel.getWeather(city!!.name)
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
        val weather = weatherViewModel.weather.collectAsState().value

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.city_detail_title, "${city?.name}"),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Country: ${city?.country}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.city_detail_population, "${city?.population}"),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.city_detail_timezone, "${city?.timezone}"),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Text(
                        text = stringResource(
                            R.string.city_detail_current_time, "${
                                ZonedDateTime.now(ZoneId.of(city?.timezone ?: "UTC")).toLocalTime()
                                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                            }"
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = stringResource(
                        R.string.city_detail_currency,
                        "${countryCurrency.currencies.joinToString(", ") { it.name + "(${it.symbol})" ?: "Unknown" }}"
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
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
            when (weather) {
                is ApiResult.Success -> {
                    val weatherData = weather.data
                    Text(
                        text = stringResource(R.string.city_detail_current_weather),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(
                                R.string.city_detail_temperature,
                                String.format("%.1f", weatherData.current.temp_c),
                                String.format("%.1f", weatherData.current.temp_f)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_feels_like,
                                String.format("%.1f", weatherData.current.feelslike_c)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_condition,
                                weatherData.current.condition.text
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_wind,
                                String.format("%.1f", weatherData.current.wind_kph),
                                weatherData.current.wind_dir
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_pressure,
                                String.format("%.1f", weatherData.current.pressure_mb)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_humidity,
                                weatherData.current.humidity
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_precipitation,
                                String.format("%.1f", weatherData.current.precip_mm)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_cloudiness,
                                weatherData.current.cloud
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.city_detail_last_updated,
                                weatherData.current.last_updated
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                is ApiResult.Loading -> {
                    Text(
                        text = stringResource(R.string.city_detail_weather_loading),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                is ApiResult.Error -> {
                    Text(
                        text = stringResource(R.string.city_detail_weather_error),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Button(
                onClick = {
                    navController.navigate(
                        Routes.getMapRoute("${city?.name}", "${city?.latitude}", "${city?.longitude}")
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.city_detail_show_map))
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