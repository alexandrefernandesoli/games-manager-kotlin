package com.example.gamesmanagerapp

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamesmanagerapp.databinding.FragmentPlayersBinding
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.repositories.PlayerRepository
import com.example.gamesmanagerapp.views.PlayerDetail

class PlayersFragment : Fragment() {
    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    private lateinit var playersRepository: PlayerRepository

    private lateinit var players: List<PlayerDetail>
    private lateinit var filteredPlayers: List<PlayerDetail>

    private lateinit var listAdapter: PlayersAdapter

    private lateinit var searchView: SearchView
    private lateinit var queryTextListener: SearchView.OnQueryTextListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playersRepository = PlayerRepository(requireContext())

        players = playersRepository.getAllPlayersWithDetail()

        setListAdapter(players)

        binding.addPlayerButton.setOnClickListener {
            findNavController().navigate(R.id.action_PlayersFragment_to_PlayersFormFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView

            searchView.queryHint = "Buscar..."
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filteredPlayers = players.filter {
                    it.name.contains(
                        newText,
                        ignoreCase = true
                    ) || it.teamDescription.contains(newText, ignoreCase = true) || it.cpf.contains(
                        newText
                    ) || it.birthYear.toString().contains(newText)
                }

                setListAdapter(filteredPlayers)

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                filteredPlayers = players.filter {
                    it.name.contains(
                        query,
                        ignoreCase = true
                    ) || it.teamDescription.contains(query, ignoreCase = true) || it.cpf.contains(
                        query
                    ) || it.birthYear.toString().contains(query)
                }

                setListAdapter(filteredPlayers)

                return true
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)


        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setListAdapter(players: List<PlayerDetail>) {
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

                setListAdapter(players)

                Toast.makeText(
                    context,
                    "Jogador removido com sucesso",
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