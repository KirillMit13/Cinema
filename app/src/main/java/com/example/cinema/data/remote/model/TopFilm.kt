package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class TopFilm(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("ratingKinopoisk") val rating: Double?,
    @SerializedName("year") val year: Int?,
    @SerializedName("genres") val genres: List<Genre>?
)
