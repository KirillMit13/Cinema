package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class SeasonsResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val items: List<SeasonItem>
)

data class SeasonItem(
    @SerializedName("number") val number: Int,
    @SerializedName("episodes") val episodes: List<EpisodeItem>
)

data class EpisodeItem(
    @SerializedName("episodeNumber") val episodeNumber: Int,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("releaseDate") val releaseDate: String?
)


