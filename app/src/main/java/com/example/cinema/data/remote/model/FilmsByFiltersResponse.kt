package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class FilmsByFiltersResponse(
    @SerializedName("items") val items: List<FilteredFilm>
)
