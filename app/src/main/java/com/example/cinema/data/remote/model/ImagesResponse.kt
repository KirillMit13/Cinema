package com.example.cinema.data.remote.model

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("items") val items: List<ImageItem>
)

data class ImageItem(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("previewUrl") val previewUrl: String,
    @SerializedName("type") val type: String?
)


