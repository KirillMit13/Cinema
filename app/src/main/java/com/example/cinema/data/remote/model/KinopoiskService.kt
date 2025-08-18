package com.example.cinema.data.remote.model

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface KinopoiskService {
    @GET("v2.2/films/collections")
    suspend fun getTopFilms(
        @Query("type") type: String,
        @Query("page") page: Int = 1
    ): TopFilmsResponse

    @GET("v2.2/films/premieres")
    suspend fun getPremieres(@Query("year") year: Int, @Query("month") month: String): PremieresResponse

    @GET("v2.2/films/filters")
    suspend fun getFilters(): FiltersResponse

    @GET("v2.1/films/search-by-keyword")
    suspend fun searchFilms(@Query("keyword") query: String, @Query("page") page: Int = 1): SearchResponse

    @GET("v2.2/films")
    suspend fun filterFilms(
        @Query("type") type: String? = null,
        @Query("order") order: String? = null,
        @Query("ratingFrom") ratingFrom: Int? = null,
        @Query("ratingTo") ratingTo: Int? = null,
        @Query("yearFrom") yearFrom: Int? = null,
        @Query("yearTo") yearTo: Int? = null,
        @Query("countries") countries: Int? = null,
        @Query("genres") genres: Int? = null,
        @Query("page") page: Int = 1
    ): FilteredFilmsResponse

    @GET("v2.2/films/{id}")
    suspend fun getFilmDetails(@Path("id") id: Int): FilmDetailsResponse

    @GET("v1/staff")
    suspend fun getStaff(@Query("filmId") filmId: Int): List<StaffItem>

    @GET("v2.2/films/{id}/similars")
    suspend fun getSimilars(@Path("id") id: Int): SimilarsResponse

    @GET("v2.2/films/{id}/images")
    suspend fun getImages(@Path("id") id: Int, @Query("type") type: String?, @Query("page") page: Int): ImagesResponse

    @GET("v1/staff/{id}")
    suspend fun getPerson(@Path("id") id: Int): PersonResponse

    @GET("v2.2/films/{id}/seasons")
    suspend fun getSeasons(@Path("id") id: Int): SeasonsResponse
}
