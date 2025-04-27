package cz.matejvana.cityscope.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.const.Routes
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.mapper.CityMapper
import cz.matejvana.cityscope.viewmodels.CityViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CityListItem(city: City, navController: NavController, cityViewModel: CityViewModel = koinViewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                cityViewModel.addRecentSearch(city)
                navController.navigate(Routes.getCityDetailRoute(city.entityId.toString()))
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = CityMapper.getDisplayName(city),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.city_detail_population, city.population),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "${getFlagEmoji(city.country)} ${city.country}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun getFlagEmoji(countryCode: String): String {
    if (countryCode.length != 2) return ""
    val uppercaseCode = countryCode.uppercase()
    val firstChar = uppercaseCode[0] - 'A' + 0x1F1E6
    val secondChar = uppercaseCode[1] - 'A' + 0x1F1E6
    return String(intArrayOf(firstChar, secondChar), 0, 2)
}