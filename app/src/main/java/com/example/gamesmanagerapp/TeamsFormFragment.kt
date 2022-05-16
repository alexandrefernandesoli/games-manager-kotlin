package com.example.gamesmanagerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamesmanagerapp.databinding.FragmentTeamsBinding
import com.example.gamesmanagerapp.databinding.FragmentTeamsFormBinding
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.repositories.TeamRepository

class TeamsFormFragment : Fragment() {
    private var _binding: FragmentTeamsFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var teamRepository: TeamRepository
    private val args: TeamsFormFragmentArgs by navArgs()
    private var isUpdating: Boolean = false
    private lateinit var editingTeam: Team

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsFormBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamRepository = TeamRepository(requireContext())

        if(args.teamId != 0){
            isUpdating = true

            editingTeam = teamRepository.getTeamById(args.teamId)

            binding.teamDescription.setText(editingTeam.description)
        }

        binding.saveButton.setOnClickListener {
            val teamDescription = binding.teamDescription.text.toString()

            if(teamDescription.trim() === ""){
                Toast.makeText(context, "A descrição do time não pode ser vazia", Toast.LENGTH_SHORT).show()
            } else {
                if(isUpdating){
                    editingTeam.description = teamDescription

                    teamRepository.updateTeam(editingTeam)
                } else {
                    teamRepository.addNewTeam(Team(teamDescription))
                }

                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}