package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("genre") val genre: String?
)
