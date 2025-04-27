package cz.matejvana.cityscope.screen.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.const.Routes
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.mapper.CityMapper
import cz.matejvana.cityscope.viewmodels.CityViewModel
import cz.matejvana.cityscope.viewmodels.FavouriteCityViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CityListItem(
    city: City,
    navController: NavController,
    cityViewModel: CityViewModel = koinViewModel(),
    favouriteCityViewModel: FavouriteCityViewModel = koinViewModel(),
    displayFavourite: Boolean = true
) {

    val favouriteCities by favouriteCityViewModel.favouriteCities.collectAsState()
    val isCityFavourite = favouriteCities.contains(city.entityId)


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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${getFlagEmoji(city.country)} ${city.country}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (displayFavourite) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (isCityFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (isCityFavourite) {
                                favouriteCityViewModel.removeFavouriteCity(city.entityId)
                            } else {
                                favouriteCityViewModel.addFavouriteCity(city.entityId)
                            }
                        }
                )
            }
        }
    }
}

fun getFlagEmoji(countryCode: String): String {
    if (countryCode.length != 2) return ""
    val uppercaseCode = countryCode.uppercase()
    val firstChar = uppercaseCode[0] - 'A' + 0x1F1E6
    val secondChar = uppercaseCode[1] - 'A' + 0x1F1E6
    return String(intArrayOf(firstChar, secondChar), 0, 2)
}