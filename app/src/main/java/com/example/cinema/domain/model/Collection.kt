package com.example.cinema.domain.model

data class Collection(
    val id: Long,
    val name: String,
    val description: String? = null,
    val filmCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
