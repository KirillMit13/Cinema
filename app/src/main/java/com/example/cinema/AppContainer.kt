package com.example.cinema

import android.content.Context
import androidx.room.Room
import com.example.cinema.data.remote.ApiClient
import com.example.cinema.data.remote.model.KinopoiskService

class AppContainer(context: Context) {
    // Retrofit (Kinopoisk API)
    val kinopoiskService: KinopoiskService = ApiClient.kinopoiskService

    // Room Database
    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "skillcinema-db"
        ).fallbackToDestructiveMigration().build()
    }

    // ViewModel-фабрики
    fun provideHomeViewModelFactory(): HomeViewModelFactory {
        return HomeViewModelFactory(
            homeRepository = HomeRepository(
                api = kinopoiskService,
                filmDao = appDatabase.filmDao()
            )
        )
    }

    fun provideCollectionsRepository(): CollectionsRepository = CollectionsRepository(
        collectionDao = appDatabase.collectionDao(),
        filmDao = appDatabase.filmDao()
    )
}