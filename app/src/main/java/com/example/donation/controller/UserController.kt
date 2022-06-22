package com.example.donation.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.entity.User
import com.example.donation.repository.UserRepository

@RequiresApi(Build.VERSION_CODES.O)
object UserController {
    private val userRepository = UserRepository

    fun findUserByEmail(email: String): User? = userRepository.findByEmail(email)

    fun register(user: User): User =
        if (!userRepository.existsByEmail(user.email)) {
            userRepository.save(user)
        } else {
            throw Exception("Usuário já cadastrado")
        }
}
