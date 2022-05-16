package com.example.gamesmanagerapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamesmanagerapp.databinding.FragmentPlayersBinding
import com.example.gamesmanagerapp.databinding.FragmentPlayersFormBinding
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.repositories.PlayerRepository
import com.example.gamesmanagerapp.repositories.TeamRepository
import java.lang.NumberFormatException
import java.util.*
import kotlin.reflect.typeOf

class PlayersFormFragment : Fragment() {
    private var _binding: FragmentPlayersFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var playersRepository: PlayerRepository
    private lateinit var teamRepository: TeamRepository
    private lateinit var teams: List<Team>
    private var isUpdating: Boolean = false
    private var editingPlayer: Player? = null

    private val args: PlayersFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersFormBinding.inflate(inflater, container, false)

        playersRepository = PlayerRepository(requireContext())
        teamRepository = TeamRepository(requireContext())
        teams = teamRepository.getAllTeams()

        if(teams.isEmpty()){
            Toast.makeText(context, "É preciso cadastrar um time antes de cadastrar jogadores", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.teamsList.adapter = adapter


        binding.saveButton.setOnClickListener { handleSubmitForm() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.v("PLAYERID", args.playerId.toString())

        if(args.playerId != 0){
            isUpdating = true

            editingPlayer = playersRepository.getPlayerById(args.playerId)
            val editingPlayerTeam = teamRepository.getTeamById(editingPlayer!!.teamId)

            binding.playerName.setText(editingPlayer!!.name)
            binding.playerCPF.setText(editingPlayer!!.cpf)
            binding.playerBirthYear.setText(editingPlayer!!.birthYear.toString())
            binding.teamsList.setSelection(teams.indexOf(editingPlayerTeam))
        }

    }

    private fun handleSubmitForm(){
        val playerName = binding.playerName.text.toString()
        val playerCPF = binding.playerCPF.text.toString()
        val playerBirthYear = try { binding.playerBirthYear.text.toString().toInt() } catch (e: NumberFormatException) { 0 }
        val selectedTeam: Team = binding.teamsList.selectedItem as Team
        val now = Calendar.getInstance().get(Calendar.YEAR)

        when {
            playerName.trim().isEmpty() -> {
                Toast.makeText(context, "Nome do jogador não deve ser vazio", Toast.LENGTH_SHORT).show()
                return
            }
            !myValidateCPF(playerCPF) -> {
                Toast.makeText(context, "É necessário digitar um cpf válido", Toast.LENGTH_SHORT).show()
                return
            }
            playerBirthYear < now - 200 || playerBirthYear > now -> {
                Toast.makeText(context, "É necessário digitar uma data de nascimento válida", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if(isUpdating && editingPlayer != null){
            val player = Player(playerName, cleanCPF(playerCPF), playerBirthYear, selectedTeam.teamId, id = editingPlayer!!.id)
            playersRepository.updatePlayer(player)
        } else {
            val player = Player(playerName, cleanCPF(playerCPF), playerBirthYear, selectedTeam.teamId)
            playersRepository.addNewPlayer(player)
        }


        findNavController().navigateUp()
    }

    private fun cleanCPF(cpf: String): String {
        return cpf.trim().replace(".", "").replace("-", "")
    }

    // Função para validação de CPF (Extra - https://medium.com/@paulo_linhares/cpf-mascara-e-validacao-kotlin-975f1e394ecb)
    private fun myValidateCPF(cpf : String) : Boolean{
        val cpfClean = cleanCPF(cpf)

        //## check if size is eleven
        if (cpfClean.length != 11)
            return false

        //## check if is number
        try {
            val number  = cpfClean.toLong()
        }catch (e : Exception){
            return false
        }

        //continue
        var dvCurrent10 = cpfClean.substring(9,10).toInt()
        var dvCurrent11= cpfClean.substring(10,11).toInt()

        //the sum of the nine first digits determines the tenth digit
        val cpfNineFirst = IntArray(9)
        var i = 9
        while (i > 0 ) {
            cpfNineFirst[i-1] = cpfClean.substring(i-1, i).toInt()
            i--
        }
        //multiple the nine digits for your weights: 10,9..2
        var sumProductNine = IntArray(9)
        var weight = 10
        var position = 0
        while (weight >= 2){
            sumProductNine[position] = weight * cpfNineFirst[position]
            weight--
            position++
        }
        //Verify the nineth digit
        var dvForTenthDigit = sumProductNine.sum() % 11
        dvForTenthDigit = 11 - dvForTenthDigit //rule for tenth digit
        if(dvForTenthDigit > 9)
            dvForTenthDigit = 0
        if (dvForTenthDigit != dvCurrent10)
            return false

        //### verify tenth digit
        var cpfTenFirst = cpfNineFirst.copyOf(10)
        cpfTenFirst[9] = dvCurrent10
        //multiple the nine digits for your weights: 10,9..2
        var sumProductTen = IntArray(10)
        var w = 11
        var p = 0
        while (w >= 2){
            sumProductTen[p] = w * cpfTenFirst[p]
            w--
            p++
        }
        //Verify the nineth digit
        var dvForeleventhDigit = sumProductTen.sum() % 11
        dvForeleventhDigit = 11 - dvForeleventhDigit //rule for tenth digit
        if(dvForeleventhDigit > 9)
            dvForeleventhDigit = 0
        if (dvForeleventhDigit != dvCurrent11)
            return false

        return true
    }
}