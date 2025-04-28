package cz.matejvana.cityscope

import cz.matejvana.cityscope.api.ApiKeys
import cz.matejvana.cityscope.api.CurrencyExchangeApi
import cz.matejvana.cityscope.api.WeatherApi
import cz.matejvana.cityscope.data.*
import cz.matejvana.cityscope.repository.*
import cz.matejvana.cityscope.viewmodels.*
import io.objectbox.BoxStore
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val repositoryModule = module {
    single { CityRepository(get(), get()) }
    single { CountryCurrencyRepository(get(), get()) }
    single { SettingsRepository(get()) }
    single { CurrencyExchangeRepository(get()) }
    single { WeatherRepository(get()) }
    single { RecentSearchRepository(get()) }
    single { FavouriteCityRepository(get()) }
    single { CountryRepository(get(), get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .name("cityscope")
            .build().also {
                println("BoxStore created")
            }
    }
    single { get<BoxStore>().boxFor(City::class.java) }
    single { get<BoxStore>().boxFor(CountryCurrency::class.java) }
    single { get<BoxStore>().boxFor(RecentCity::class.java) }
    single { get<BoxStore>().boxFor(FavouriteCity::class.java) }
    single { get<BoxStore>().boxFor(Country::class.java) }
}

val viewModelModule = module {
    viewModel { CityViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { CurrencyExchangeViewModel(get(), get()) }
    viewModel { WeatherViewModel(get()) }
    viewModel { FavouriteCityViewModel(get(), get()) }
    viewModel { CountryViewModel(get(), get()) }
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

