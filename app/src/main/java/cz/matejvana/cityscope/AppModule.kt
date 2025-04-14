package cz.matejvana.cityscope

import cz.matejvana.cityscope.data.City
import cz.matejvana.cityscope.data.MyObjectBox
import cz.matejvana.cityscope.repository.CityRepository
import cz.matejvana.cityscope.viewmodels.CityViewModel
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { CityRepository(get(), get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .name("cityscope")
            .build()
    }
    single { get<BoxStore>().boxFor(City::class.java) }
}

val viewModelModule = module {
    single { CityViewModel(get()) }
}