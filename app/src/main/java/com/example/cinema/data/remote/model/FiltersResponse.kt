package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class FiltersResponse(
    @SerializedName("genres") val genres: List<GenreItem>,
    @SerializedName("countries") val countries: List<CountryItem>
)

data class GenreItem(
    @SerializedName("id") val id: Int,
    @SerializedName("genre") val name: String
)

data class CountryItem(
    @SerializedName("id") val id: Int,
    @SerializedName("country") val name: String
)
