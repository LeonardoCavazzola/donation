package com.example.donation.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.config.SessionManager
import com.example.donation.entity.Collect
import com.example.donation.entity.User
import com.example.donation.repository.CollectRepository
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object CollectController {
    private val sessionManager = SessionManager
    private val collectRepository = CollectRepository

    fun findCompatibleWithUser(): List<Collect> {
        val date = sessionManager.authenticatedUser.lastDonation.minusDays(90)
        return collectRepository.findAfterDate(date)
    }

    fun findAll(): List<Collect> = collectRepository.findAll()

    fun create(collect: Collect): Collect {
        val collectEnriched = collect.copy(requester = sessionManager.authenticatedUser)
        return collectRepository.save(collectEnriched)
    }

    fun update(id: UUID, collect: Collect): Collect {
        val updated = collect.let { collectRepository.findById(id)!!.update(collect) }
        return collectRepository.save(updated)
    }

    fun delete(id: UUID) = collectRepository.delete(id)

    fun addDonor(collectId: UUID, donor: User) = collectRepository.findById(collectId)
        ?.addDonor(donor)
        ?.let { collectRepository.save(it) }
}
