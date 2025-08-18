package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class StaffItem(
    @SerializedName("staffId") val staffId: Int,
    @SerializedName("nameRu") val nameRu: String?,
    @SerializedName("nameEn") val nameEn: String?,
    @SerializedName("description") val description: String?, // role/character
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("professionText") val professionText: String?,
    @SerializedName("professionKey") val professionKey: String?
)


