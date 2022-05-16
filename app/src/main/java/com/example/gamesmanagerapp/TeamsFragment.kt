package com.example.gamesmanagerapp

import android.os.Bundle
import android.view.*
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamesmanagerapp.databinding.FragmentTeamsBinding
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.repositories.PlayerRepository
import com.example.gamesmanagerapp.repositories.TeamRepository

class TeamsFragment : Fragment() {
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var teamRepository: TeamRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var listAdapter: TeamsAdapter
    private lateinit var teams: List<Team>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        teamRepository = TeamRepository(requireContext())

        setListAdapter()

        binding.addTeamButton.setOnClickListener {
            findNavController().navigate(R.id.action_TeamsFragment_to_TeamsFormFragment)
        }

        return binding.root
    }

    private fun setListAdapter(){
        teams = teamRepository.getAllTeams()

        listAdapter = TeamsAdapter(requireContext(), teams)

        registerForContextMenu(binding.teamsList)
        binding.teamsList.adapter = listAdapter
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
        val info = item.menuInfo as AdapterContextMenuInfo

        val selectedTeam: Team = teams[info.position]

        when (item.itemId) {
            R.id.actionDelete -> {
                playerRepository = PlayerRepository(requireContext())

                val teamPlayers = playerRepository.getPlayersByTeamId(selectedTeam.teamId)

                if(teamPlayers.isNotEmpty()){
                    Toast.makeText(context,"Time ${selectedTeam.description} não pode ser removido, pois possui jogadores", Toast.LENGTH_SHORT).show()

                    return false
                }

                teamRepository.deleteTeam(selectedTeam)

                setListAdapter()

                Toast.makeText(context,"Time ${selectedTeam.description} foi removido com sucesso", Toast.LENGTH_SHORT).show()

                return true
            }
            R.id.actionEdit -> {
                val action = TeamsFragmentDirections.actionTeamsFragmentToTeamsFormFragment(selectedTeam.teamId)
                findNavController().navigate(action)

                return true
            }
            else -> {
                return super.onContextItemSelected(item)
            }
        }
    }
}