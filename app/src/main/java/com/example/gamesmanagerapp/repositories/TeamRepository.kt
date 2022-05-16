package com.example.gamesmanagerapp.repositories

import android.content.Context
import com.example.gamesmanagerapp.database.AppDatabase
import com.example.gamesmanagerapp.entities.Team

class TeamRepository(context: Context) {
    val db: AppDatabase =  AppDatabase.invoke(context)

    fun getAllTeams(): List<Team>{
        return db.teamDao().getAll()
    }

    fun getTeamById(teamId: Int): Team{
        return db.teamDao().getOneById(teamId)
    }

    fun addNewTeam(team: Team) {
        db.teamDao().insertAll(team)
    }

    fun deleteTeam(team: Team) {
        db.teamDao().delete(team)
    }

    fun updateTeam(team: Team) {
        db.teamDao().update(team)
    }
}