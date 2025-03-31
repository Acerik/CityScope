package cz.matejvana.cityscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.matejvana.cityscope.data.MyObjectBox
import cz.matejvana.cityscope.screen.NavigationDrawerMenu
import cz.matejvana.cityscope.ui.theme.CityScopeTheme
import io.objectbox.BoxStore

class MainActivity : ComponentActivity() {

    private lateinit var boxStore: BoxStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityScopeTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        boxStore = MyObjectBox.builder()
            .androidContext(this)
            .name("cityscope")
            .build()
        val cityRepository = CityRepository(boxStore, this)
        cityRepository.initializeData()
        //todo fix reloading data on each start
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            NavigationDrawerMenu(
                content = {
                    Text("Menutext", modifier = Modifier.padding(16.dp))
                },
                navController = navController
            )
        },
        content = { paddingValues ->
            Text(
                "CityScope", modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )
}