package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("pagesCount") val pagesCount: Int?,
    @SerializedName("films") val items: List<SearchFilm>
)

data class SearchFilm(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("posterUrlPreview") val posterUrl: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("year") val year: String?
)


