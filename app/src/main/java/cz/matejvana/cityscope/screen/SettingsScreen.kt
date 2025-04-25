import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.matejvana.cityscope.R
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = koinViewModel()) {
    val preferredCurrencyCode by settingsViewModel.preferredCurrencyCode.collectAsState()
    val allCurrencyCodes = remember { settingsViewModel.getAllCurrencyCodes() }
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("") }

    LaunchedEffect(preferredCurrencyCode) {
        selectedCurrency = preferredCurrencyCode ?: ""
    }

    //todo need to fix saving currency code

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_select_currency_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (selectedCurrency.isNotEmpty()) selectedCurrency else stringResource(R.string.settings_select_currency))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allCurrencyCodes.forEach { currencyCode ->
                    DropdownMenuItem(
                        text = { Text("${currencyCode.symbol} | ${currencyCode.name}") },
                        onClick = {
                            selectedCurrency = currencyCode.code
                            expanded = false
                            settingsViewModel.savePreferredCurrencyCode(currencyCode.code)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.settings_save_back))
        }
    }
}