package cz.matejvana.cityscope

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import cz.matejvana.cityscope.api.CurrencyExchangeApi
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.MyObjectBox
import cz.matejvana.cityscope.repository.CityRepository
import cz.matejvana.cityscope.repository.CountryCurrencyRepository
import cz.matejvana.cityscope.repository.CurrencyExchangeRepository
import cz.matejvana.cityscope.repository.SettingsRepository
import cz.matejvana.cityscope.viewmodels.CityViewModel
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single { CityRepository(get(), get()) }
    single { CountryCurrencyRepository(get(), get()) }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }
    single { SettingsRepository(get()) }
    single { CurrencyExchangeRepository(get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .name("cityscope")
            .build()
    }
    single { get<BoxStore>().boxFor(City::class.java) }
    single { get<BoxStore>().boxFor(CountryCurrency::class.java) }
}

val viewModelModule = module {
    single { CityViewModel(get(), get()) }
    single { SettingsViewModel(get(), get()) }
    single { CurrencyExchangeViewModel(get(), get()) }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(CurrencyExchangeApi::class.java)
    }
}