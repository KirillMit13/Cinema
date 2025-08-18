package com.example.cinema.data.remote

import com.example.cinema.ApiKeyInterceptor
import com.example.cinema.BuildConfig
import com.example.cinema.data.remote.model.KinopoiskService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    init {
        println("ApiClient: Initializing with API key: ${BuildConfig.KINOPOISK_API_KEY.take(8)}...")
        if (BuildConfig.KINOPOISK_API_KEY.isBlank() || BuildConfig.KINOPOISK_API_KEY == "null") {
            println("ApiClient: WARNING! API key is not set or invalid!")
        }
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/api/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ApiKeyInterceptor(BuildConfig.KINOPOISK_API_KEY))
                    .addInterceptor(loggingInterceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val kinopoiskService: KinopoiskService by lazy {
        retrofit.create(KinopoiskService::class.java)
    }
}