package com.example.gamesmanagerapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team

class TeamsAdapter(context: Context?, teams: List<Team>): BaseAdapter() {
    private val teams: List<Team>
    private val context: Context?

    init {
        this.teams = teams
        this.context = context
    }

    override fun getCount(): Int{
        return teams.size
    }

    override fun getItem(position: Int): Team {
        return teams[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.adapter_teams, null)

        val team: Team = getItem(position)

        val teamName = view.findViewById<TextView>(R.id.teamName)


        teamName.text = team.description


        return view
    }
}