package com.example.gamesmanagerapp

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamesmanagerapp.databinding.FragmentPlayersBinding
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.repositories.PlayerRepository
import com.example.gamesmanagerapp.repositories.TeamRepository
import com.example.gamesmanagerapp.views.PlayerDetail

class PlayersFragment : Fragment() {
    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    private lateinit var playersRepository: PlayerRepository
    private lateinit var teamRepository: TeamRepository
    private lateinit var players: List<PlayerDetail>
    private lateinit var teams: List<Team>
    private lateinit var listAdapter: PlayersAdapter
    private var isInitial = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playersRepository = PlayerRepository(requireContext())
        teamRepository = TeamRepository(requireContext())

        teams = teamRepository.getAllTeams()
        players = playersRepository.getAllPlayersWithDetail()

        setListAdapter()

        teams = teams.plus(Team("Todos", 0))
        teams = teams.sortedBy { it.teamId }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTeams.adapter = adapter

        binding.addPlayerButton.setOnClickListener {
            findNavController().navigate(R.id.action_PlayersFragment_to_PlayersFormFragment)
        }

        binding.spinnerTeams.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(!isInitial){
                    filterButtonHandler()
                }
                isInitial = false
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterButtonHandler() {
        val selectedSpinnerItem = binding.spinnerTeams.selectedItem as Team



        players = if(selectedSpinnerItem.teamId == 0){
            playersRepository.getAllPlayersWithDetail()
        } else {
            playersRepository.getPlayersByTeamId(selectedSpinnerItem.teamId)
        }

        setListAdapter()
    }

    private fun setListAdapter() {
        listAdapter = PlayersAdapter(requireContext(), players)

        registerForContextMenu(binding.playersList)
        binding.playersList.adapter = listAdapter
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        requireActivity().menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        val selectedPlayer: PlayerDetail = players[info.position]

        when (item.itemId) {
            R.id.actionDelete -> {
                playersRepository.deletePlayer(
                    Player(
                        name = selectedPlayer.name,
                        cpf = selectedPlayer.cpf,
                        id = selectedPlayer.id,
                        teamId = selectedPlayer.teamId,
                        birthYear = selectedPlayer.birthYear
                    )
                )

                players = players.filter { it.id != selectedPlayer.id }

                setListAdapter()

                Toast.makeText(
                    context,
                    "Jogador ${selectedPlayer.name} foi removido com sucesso",
                    Toast.LENGTH_SHORT
                ).show()

                return true
            }
            R.id.actionEdit -> {
                val action = PlayersFragmentDirections.actionPlayersFragmentToPlayersFormFragment(
                    selectedPlayer.id
                )
                findNavController().navigate(action)

                return true
            }
            else -> {
                return super.onContextItemSelected(item)
            }
        }
    }
}