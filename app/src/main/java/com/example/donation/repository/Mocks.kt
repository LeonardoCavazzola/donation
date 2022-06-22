package com.example.donation.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.entity.Collect
import com.example.donation.entity.User
import com.example.donation.extetion.toLocalDate

import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object Mocks {
    val user1 = User(
        id = UUID.randomUUID(),
        name = "user1",
        email = "a@a.com",
        password = "1",
        phone = "54",
        bloodType = "O+",
        lastDonation = "10012022".toLocalDate(),
    )

    val user2 = User(
        id = UUID.randomUUID(),
        name = "user2",
        email = "2@a.com",
        password = "1",
        phone = "5499999999",
        bloodType = "O-",
        lastDonation = "22062022".toLocalDate(),
    )

    val user3 = User(
        id = UUID.randomUUID(),
        name = "user3",
        email = "3@a.com",
        password = "1",
        phone = "5499999999",
        bloodType = "AB+",
        lastDonation = "01011990".toLocalDate(),
    )

//================================================================================

    val collect1 = Collect(
        id = UUID.randomUUID(),
        bloodType = "B+",
        date = "01122022".toLocalDate(),
        donorExpectation = 20,
        requester = user1
    )

    val collect2 = Collect(
        id = UUID.randomUUID(),
        bloodType = "O-",
        date = "01112022".toLocalDate(),
        donorExpectation = 20,
        requester = user2
    )

    val collect3 = Collect(
        id = UUID.randomUUID(),
        bloodType = "A+",
        date = "22062022".toLocalDate(),
        donorExpectation = 20,
        requester = user3
    )

    val collect4 = Collect(
        id = UUID.randomUUID(),
        bloodType = "AB+",
        date = "01012023".toLocalDate(),
        donorExpectation = 20,
        requester = user1
    )

    val collect5 = Collect(
        id = UUID.randomUUID(),
        bloodType = "O-",
        date = "01012023".toLocalDate(),
        donorExpectation = 20,
        requester = user2
    )
}

