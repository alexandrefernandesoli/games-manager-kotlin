package com.example.gamesmanagerapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player (
    val name: String,
    val cpf: String,
    val birthYear: Int,
    val teamId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
        )
