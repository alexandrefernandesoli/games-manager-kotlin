package com.example.gamesmanagerapp.repositories

import android.content.Context
import com.example.gamesmanagerapp.database.AppDatabase
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.views.PlayerDetail

class PlayerRepository(context: Context) {
    val db: AppDatabase =  AppDatabase.invoke(context)

    fun getAllPlayers(): List<Player>{
        return db.playerDao().getAll()
    }

    fun getAllPlayersWithDetail(): List<PlayerDetail>{
        return db.playerDao().getAllWithDetails()
    }

    fun getPlayerById(playerId: Int): Player {
        return db.playerDao().getOneById(playerId)
    }

    fun getPlayersByTeamId(teamId: Int): List<PlayerDetail> {
        return db.playerDao().getPlayersFromTeam(teamId)
    }

    fun isCpfRegistered(cpf: String): Boolean {
        val player = db.playerDao().getPlayerWithCPF(cpf)

        return player != null
    }

    fun deletePlayer(player: Player) {
        db.playerDao().delete(player)
    }

    fun updatePlayer(player: Player) {
        db.playerDao().update(player)
    }

    fun addNewPlayer(player: Player) {
        db.playerDao().insertAll(player)
    }
}