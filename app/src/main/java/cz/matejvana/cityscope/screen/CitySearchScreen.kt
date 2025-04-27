package cz.matejvana.cityscope.screen

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
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.viewmodels.CityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CitySearchScreen(
    navController: NavController,
    viewModel: CityViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<City>()) }

    val recentSearchCities by viewModel.recentSearches.collectAsState()

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
                CityListItem(
                    city,
                    navController
                )
            }
        }
        if (searchResults.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.city_search_last_seen),
                style = MaterialTheme.typography.bodyLarge
            )
            LazyColumn {
                items(recentSearchCities) { city ->
                    CityListItem(
                        city,
                        navController
                    )
                }
            }
        }
    }
}