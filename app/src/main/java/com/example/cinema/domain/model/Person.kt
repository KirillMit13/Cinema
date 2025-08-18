package com.example.cinema.domain.model

data class Person(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val role: String? = null,
    val profession: String? = null
)
