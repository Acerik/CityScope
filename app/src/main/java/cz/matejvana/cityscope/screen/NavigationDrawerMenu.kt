package cz.matejvana.cityscope.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.const.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerMenu(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
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
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.menu_search_city)) },
                        icon = { Icon(Icons.Filled.Menu, contentDescription = null) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(Routes.CITY_SEARCH)
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CityScope") },
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
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.CITY_SEARCH) {
        composable(Routes.CITY_SEARCH) { CitySearchScreen(navController) }
        composable(Routes.getCityDetailRoute("{cityId}")) {
            val cityId = it.arguments?.getString("cityId")
            if (cityId != null) {
                CityDetailScreen(navController, cityId.toLong())
            }
            //todo handle else
        }
    }
}