package com.example.donation.entity

import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

data class Collect(
    val id: UUID = UUID.randomUUID(),
    val bloodType: String,
    val date: LocalDate,
    val donorExpectation: Int,
    val requester: User? = null,
    val donors: List<User> = emptyList(),
) : Serializable {
    val remainingDonors: Int get() = donorExpectation - donors.size

    fun addDonor(donor: User): Collect = copy(donors = donors + donor)

    fun update(collect: Collect): Collect = copy(
        bloodType = collect.bloodType,
        date = collect.date,
        donorExpectation = collect.donorExpectation,
    )
}
