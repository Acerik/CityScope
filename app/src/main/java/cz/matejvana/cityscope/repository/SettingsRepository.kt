package cz.matejvana.cityscope.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import cz.matejvana.cityscope.const.SettingsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    init {
        // Launch a coroutine to initialize the DataStore
        kotlinx.coroutines.GlobalScope.launch {
            initializeDataStore()
        }
    }

    private suspend fun initializeDataStore() {
        dataStore.edit { preferences ->
            if (preferences[SettingsKeys.PREFERRED_CURRENCY_CODE] == null) {
                preferences[SettingsKeys.PREFERRED_CURRENCY_CODE] = "eur" // Default value
            }
        }
    }

    fun getPreferredCurrencyCode(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[SettingsKeys.PREFERRED_CURRENCY_CODE]
        }
    }

    suspend fun savePreferredCurrencyCode(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[SettingsKeys.PREFERRED_CURRENCY_CODE] = currencyCode
        }
    }
}