package com.example.gamesmanagerapp.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team


data class TeamWithPlayers(
    @Embedded val team: Team,

    @ColumnInfo(name = "players_count") var playerCount: Int? = null,

    @Relation(
        parentColumn = "teamId",
        entityColumn = "id"
    )
    val players: List<Player>
)