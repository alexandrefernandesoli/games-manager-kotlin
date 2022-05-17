package com.example.gamesmanagerapp.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["cpf"], unique = true)])
data class Player (
    val name: String,
    val cpf: String,
    val birthYear: Int,
    val teamId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
        )
