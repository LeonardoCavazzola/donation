package com.example.donation.entity

import java.io.Serializable
import java.time.LocalDate
import java.util.*

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val bloodType: String,
    val lastDonation: LocalDate
) : Serializable {
}
