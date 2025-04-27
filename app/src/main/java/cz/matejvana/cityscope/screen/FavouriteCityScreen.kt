package cz.matejvana.cityscope.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cz.matejvana.cityscope.viewmodels.FavouriteCityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteCityScreen(
    navController: NavController,
    favouriteCityViewModel: FavouriteCityViewModel = koinViewModel()
) {

    val favouriteCities = favouriteCityViewModel.getAllFavouriteCities()

    LazyColumn {
        items(favouriteCities) { city ->
            CityListItem(city, navController)
        }
    }
}