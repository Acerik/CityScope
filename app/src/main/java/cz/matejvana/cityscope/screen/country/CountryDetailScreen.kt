package cz.matejvana.cityscope.screen.country

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.getCurrentLocale
import cz.matejvana.cityscope.mapper.CurrencyMapper
import cz.matejvana.cityscope.mapper.NumberMapper
import cz.matejvana.cityscope.screen.ExchangeRateText
import cz.matejvana.cityscope.viewmodels.CountryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CountryDetailScreen(
    navController: NavController,
    countryId: Long,
    countryViewModel: CountryViewModel = koinViewModel(),
) {

    val country = countryViewModel.getCountryById(countryId)

    if (country != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.country_detail_title, country.nameOfficial),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(
                        R.string.country_detail_official_name, country.getOfficialNameByLocale(
                            getCurrentLocale(countryViewModel.getContext())
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(
                        R.string.country_detail_common_name,
                        country.getCommonNameByLocale(getCurrentLocale(countryViewModel.getContext()))
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.country_detail_country_code, country.countryCode),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(
                        R.string.country_detail_population,
                        NumberMapper.formatWithSpaces(country.population)
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.country_detail_area, NumberMapper.formatWithSpaces(country.area)),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.country_detail_region, country.region, country.subregion),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.country_detail_capital, country.capital.joinToString(", ")),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.country_detail_domain, country.tld.joinToString(", ")),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = CurrencyMapper.mapCurrencyInfoListForDisplay(country.currencies),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (country.currencies.isNotEmpty()) {
                    ExchangeRateText(CountryCurrency(0, "", country.currencies))
                }
            }
        }
    } else {
        Text(
            text = "Stát nenalezen",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}