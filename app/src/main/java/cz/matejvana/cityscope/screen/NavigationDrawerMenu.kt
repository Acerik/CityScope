package cz.matejvana.cityscope.screen

import SettingsScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.const.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerMenu(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val gestureEnabled = !(Routes.isMap(currentRoute) || Routes.isCityDetail(currentRoute))

    ModalNavigationDrawer(
        gesturesEnabled = gestureEnabled,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        stringResource(R.string.menu_title),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()
                    Spacer(Modifier.height(10.dp))
                    MyMenuItem(
                        scope, drawerState, navController,
                        stringResource(R.string.menu_search_city),
                        Icons.Filled.Search,
                        Routes.CITY_SEARCH
                    )
                    Spacer(Modifier.height(10.dp))
                    MyMenuItem(
                        scope = scope,
                        drawerState = drawerState,
                        navController = navController,
                        label = stringResource(R.string.menu_favourites),
                        icon = Icons.Filled.Star,
                        route = Routes.FAVOURITE_CITIES
                    )
                    Spacer(Modifier.height(10.dp))
                    MyMenuItem(
                        scope = scope,
                        drawerState = drawerState,
                        navController = navController,
                        label = stringResource(R.string.menu_settings),
                        icon = Icons.Filled.Settings,
                        route = Routes.SETTINGS
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                if (Routes.isRouteWithDisabledMenu(currentRoute)) {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                } else {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    Navigation(navController)
                }
            }
        )
    }
}

@Composable
fun MyMenuItem(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
    label: String,
    icon: ImageVector,
    route: String
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = null) },
        selected = false,
        onClick = {
            scope.launch {
                drawerState.close()
            }
            navController.navigate(route)
        }
    )
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.CITY_SEARCH) {
        composable(Routes.CITY_SEARCH) { CitySearchScreen(navController) }
        composable(Routes.getCityDetailRoute("{cityId}")) {
            val cityId = it.arguments?.getString("cityId")
            if (cityId != null) {
                CityDetailScreen(navController, cityId.toLong())
            }
        }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(
            Routes.getMapRoute("{cityName}", "{latitude}", "{longitude}")
        ) {
            val cityName = it.arguments?.getString("cityName") ?: "Unknown"
            val latitude = it.arguments?.getString("latitude")?.toDoubleOrNull() ?: 0.0
            val longitude = it.arguments?.getString("longitude")?.toDoubleOrNull() ?: 0.0
            MapScreen(navController, cityName, latitude, longitude)
        }
        composable(Routes.FAVOURITE_CITIES) {
            FavouriteCityScreen(navController)
        }
    }
}
