package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class TopFilmsResponse(
    @SerializedName("items") val items: List<TopFilm>
)
