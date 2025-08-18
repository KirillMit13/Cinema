package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("personId") val personId: Int,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("profession") val profession: String?,
    @SerializedName("facts") val facts: List<String>?,
    @SerializedName("films") val films: List<PersonFilm>?
)

data class PersonFilm(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val title: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("professionKey") val professionKey: String?
)


