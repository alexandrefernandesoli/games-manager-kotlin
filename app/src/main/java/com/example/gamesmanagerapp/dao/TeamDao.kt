package com.example.gamesmanagerapp.dao

import androidx.room.*
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.entities.TeamWithPlayers

@Dao
interface TeamDao {
    @Query("SELECT * FROM team")
    fun getAll(): List<Team>


    @Query("SELECT * FROM team WHERE teamId == (:teamId) LIMIT 1")
    fun getOneById(teamId: Int): Team

    @Insert
    fun insertAll(vararg teams: Team)

    @Delete
    fun delete(team: Team)

    @Update
    fun update(team: Team)
}