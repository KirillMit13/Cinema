package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class SimilarsResponse(
    @SerializedName("items") val items: List<SimilarFilm>
)

data class SimilarFilm(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrlPreview") val posterUrl: String?
)


