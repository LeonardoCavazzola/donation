package com.example.donation.config

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.entity.User
import com.example.donation.controller.UserController

@RequiresApi(Build.VERSION_CODES.O)
object SessionManager {
    private val userService = UserController
    private var user: User? =  null
    
    val authenticatedUser: User get() = user ?: throw Exception(FAIL_GET_AUTH_MESSAGE)

    fun login(email: String, password: String) = userService.findUserByEmail(email)
        ?.takeIf { it.password == password }
        ?.let { user = it }
        ?: throw Exception(FAIL_LOGIN_MESSAGE)

    fun logoff() = run { user = null }

    private const val FAIL_LOGIN_MESSAGE = "Email ou senha incorretos"
    private const val FAIL_GET_AUTH_MESSAGE = "Você deveria estar logado para fazer isso"
}
