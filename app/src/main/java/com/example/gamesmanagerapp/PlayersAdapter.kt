package com.example.gamesmanagerapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.views.PlayerDetail

class PlayersAdapter(context: Context?, players: List<PlayerDetail>): BaseAdapter() {
    private val players: List<PlayerDetail>
    private val context: Context?

    init {
        this.players = players
        this.context = context
    }

    override fun getCount(): Int{
        return players.size
    }

    override fun getItem(position: Int): PlayerDetail {
        return players[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.adapter_players, null)

        val player: PlayerDetail = getItem(position)

        val playerName = view.findViewById<TextView>(R.id.playerName)
        val playerCPF = view.findViewById<TextView>(R.id.playerCPF)
        val playerBirthYear = view.findViewById<TextView>(R.id.playerBirthYear)
        val playerTeam = view.findViewById<TextView>(R.id.playerTeam)

        playerName.text = player.name
        playerCPF.text = player.cpf
        playerBirthYear.text = player.birthYear.toString()
        playerTeam.text = player.teamDescription

        return view
    }
}