package com.example.gamesmanagerapp.views

import androidx.room.DatabaseView


@DatabaseView("SELECT player.id, player.name, player.cpf, player.birthYear, player.teamId," +
        "team.description AS teamDescription FROM player " +
        "INNER JOIN team ON player.teamId = team.teamId")
data class PlayerDetail(
    val id: Int,
    val name: String,
    val cpf: String,
    val birthYear: Int,
    val teamId: Int,
    val teamDescription: String
)