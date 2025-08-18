package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class FilteredFilm(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("ratingKinopoisk") val rating: Double?,
    @SerializedName("year") val year: Int?,
    @SerializedName("genres") val genres: List<Genre>?
)

data class FilteredFilmsResponse(
    @SerializedName("items") val items: List<FilteredFilm>
)