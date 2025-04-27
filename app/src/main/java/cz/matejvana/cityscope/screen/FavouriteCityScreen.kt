package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.viewmodels.FavouriteCityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteCityScreen(
    navController: NavController,
    favouriteCityViewModel: FavouriteCityViewModel = koinViewModel()
) {

    val favouriteCities = favouriteCityViewModel.getAllFavouriteCities()

    Column {
        Text(
            text = stringResource(R.string.menu_favourites),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        LazyColumn {
            items(favouriteCities) { city ->
                CityListItem(city, navController)
            }
        }
    }
}