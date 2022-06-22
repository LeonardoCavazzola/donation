package com.example.donation.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.entity.User
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object UserRepository {
    private val data = mutableMapOf<UUID, User>(
        Mocks.user1.let { it.id to it },
        Mocks.user2.let { it.id to it },
        Mocks.user3.let { it.id to it },
    )

    fun existsByEmail(email: String): Boolean = data.values.find { it.email == email }?.let { true } ?: false

    fun findByEmail(email: String): User? = data.values.find { it.email == email }

    fun save(user: User): User = user.also { data[user.id] = user }
}
