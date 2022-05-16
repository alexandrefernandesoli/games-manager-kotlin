package com.example.gamesmanagerapp.dao

import androidx.room.*
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.views.PlayerDetail

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player")
    fun getAll(): List<Player>

    @Query("SELECT * FROM PlayerDetail")
    fun getAllWithDetails(): List<PlayerDetail>

    @Query("SELECT * FROM player WHERE id == (:playerId) LIMIT 1")
    fun getOneById(playerId: Int): Player

    @Query("SELECT * FROM PlayerDetail WHERE teamId == (:teamId)")
    fun getPlayersFromTeam(teamId: Int): List<PlayerDetail>

    @Insert
    fun insertAll(vararg players: Player)

    @Delete
    fun delete(player: Player)

    @Update
    fun update(player: Player)
}