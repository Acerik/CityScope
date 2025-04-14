package cz.matejvana.cityscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import cz.matejvana.cityscope.screen.NavigationDrawerMenu
import cz.matejvana.cityscope.ui.theme.CityScopeTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityScopeTheme {
                val navController = rememberNavController()
                NavigationDrawerMenu(navController)
            }
        }
        startKoin {
            androidContext(this@MainActivity)
            modules(repositoryModule, objectBoxModule, viewModelModule)
        }
    }
}

