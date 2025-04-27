package cz.matejvana.cityscope.screen.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.const.Routes
import cz.matejvana.cityscope.data.Country
import cz.matejvana.cityscope.getCurrentLocale
import cz.matejvana.cityscope.screen.city.getFlagEmoji

@Composable
fun CountryListItem(navController: NavController, country: Country) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Routes.getCountryDetailRoute(country.id.toString()))
            }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = country.nameOfficial,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = country.translations[getCurrentLocale(navController.context)]?.official ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = country.region,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = getFlagEmoji(country.countryCode),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}