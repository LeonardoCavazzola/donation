package com.example.donation.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.donation.entity.Collect
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object CollectRepository {
    private val data = mutableMapOf<UUID, Collect>(
        Mocks.collect1.let { it.id to it },
        Mocks.collect2.let { it.id to it },
        Mocks.collect3.let { it.id to it },
        Mocks.collect4.let { it.id to it },
        Mocks.collect5.let { it.id to it },
    )

    fun findById(id: UUID): Collect? = data[id]

    fun findAfterDate(date: LocalDate): List<Collect> = data.values.filter { it.date > date }

    fun findAll(): List<Collect> = data.map { it.value }

    fun save(collect: Collect): Collect = collect.also { data[collect.id] = collect }

    fun delete(id: UUID) = data.remove(id)
}
