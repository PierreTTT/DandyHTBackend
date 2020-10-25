package com.example.parkingfinder.models

data class ParkingState(
    val id: Int,
    val image: String,
    val longitude: String,
    val lattitude: String,
    val taken: Boolean,
    val createdAt: String,
    val updatedAt: String
)