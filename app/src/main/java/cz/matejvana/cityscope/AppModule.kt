package cz.matejvana.cityscope

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import cz.matejvana.cityscope.api.ApiKeys
import cz.matejvana.cityscope.api.CurrencyExchangeApi
import cz.matejvana.cityscope.api.WeatherApi
import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.CountryCurrency
import cz.matejvana.cityscope.data.MyObjectBox
import cz.matejvana.cityscope.data.RecentCity
import cz.matejvana.cityscope.repository.*
import cz.matejvana.cityscope.viewmodels.CityViewModel
import cz.matejvana.cityscope.viewmodels.CurrencyExchangeViewModel
import cz.matejvana.cityscope.viewmodels.SettingsViewModel
import cz.matejvana.cityscope.viewmodels.WeatherViewModel
import io.objectbox.BoxStore
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

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
    single { WeatherRepository(get()) }
    single { RecentSearchRepository(get()) }
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
    single { get<BoxStore>().boxFor(RecentCity::class.java) }
}

val viewModelModule = module {
    single { CityViewModel(get(), get(), get()) }
    single { SettingsViewModel(get(), get()) }
    single { CurrencyExchangeViewModel(get(), get()) }
    single { WeatherViewModel(get()) }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyExchangeApi::class.java)
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(androidContext().cacheDir))
            .build()
            .create(WeatherApi::class.java)
    }
}

fun provideOkHttpClient(cacheDir: File): OkHttpClient {
    val cacheSize = 10L * 1024 * 1024 // 10 MB
    val cache = Cache(cacheDir, cacheSize)
    val cacheInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val urlWithApiKey = originalRequest.url.newBuilder()
            .addQueryParameter("key", ApiKeys.WEATHER_API_KEY)
            .build()
        val requestWithApiKey = originalRequest.newBuilder().url(urlWithApiKey).build()
        val response: Response = chain.proceed(requestWithApiKey)
        val maxAge = 60 * 60 // 60 minut v sekundách
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()

    }


    return OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(cacheInterceptor)
        .build()
}

