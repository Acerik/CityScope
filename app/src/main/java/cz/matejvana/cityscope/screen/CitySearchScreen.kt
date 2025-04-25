package cz.matejvana.cityscope.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.const.Routes
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.mapper.CityMapper
import cz.matejvana.cityscope.viewmodels.CityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CitySearchScreen(navController: NavController, viewModel: CityViewModel = koinViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<City>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.enter_city_name_search)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchResults = viewModel.searchCitiesByName(searchQuery)
                }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                searchResults = viewModel.searchCitiesByName(searchQuery)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.search))
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(searchResults) { city ->
                CitySearchItem(city, navController)
            }
        }
    }
}

@Composable
fun CitySearchItem(city: City, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Routes.getCityDetailRoute(city.entityId.toString())) }
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