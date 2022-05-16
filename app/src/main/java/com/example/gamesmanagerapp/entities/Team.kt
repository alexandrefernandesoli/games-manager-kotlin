package com.example.gamesmanagerapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team(
    var description: String,
    @PrimaryKey(autoGenerate = true)
    val teamId: Int = 0
) {
    override fun toString(): String {
        return description
    }
}