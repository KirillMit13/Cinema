package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class PremieresResponse(
    @SerializedName("items") val items: List<PremiereFilm>
)
