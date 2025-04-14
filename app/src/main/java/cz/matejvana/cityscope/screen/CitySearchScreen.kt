package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // Simulate search and update results
                searchResults = viewModel.searchCitiesByName(searchQuery)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(searchResults) { city ->
                Text(text = CityMapper.getDisplayName(city), modifier = Modifier.padding(8.dp))
            }
        }
    }
}