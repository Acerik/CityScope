package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.viewmodels.CityViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CityDetailScreen(navController: NavController, cityId: Long, cityViewModel: CityViewModel = koinViewModel()) {
    //todo implement detail screen
    // implementation should have all city infromation
    // add currency exchange
    // add weather
    // add map
    val city by remember { mutableStateOf(cityViewModel.getCityById(cityId)) }

    if (city != null) {
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
        }
    } else {
        Text(
            text = "City not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}