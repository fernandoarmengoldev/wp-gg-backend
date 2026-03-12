package com.fernandoarmengol.ggwp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fernandoarmengol.ggwp.GlobalVar.Companion.match
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.FragmentGeneralBinding
import com.fernandoarmengol.ggwp.model.match_nodejs.Participants
import com.fernandoarmengol.ggwp.ui.adapters.RVChampionsAdapter
import com.fernandoarmengol.ggwp.ui.adapters.RVParticipantsAdapter

class GeneralFragment : Fragment() {
    lateinit var binding: FragmentGeneralBinding

    private val adapter0: RVParticipantsAdapter = RVParticipantsAdapter()
    private val adapter1: RVParticipantsAdapter = RVParticipantsAdapter()

    var equipo0participants = mutableListOf<Participants>()
    var equipo1participants = mutableListOf<Participants>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneralBinding.inflate(inflater, container, false)
        val view = binding.root

        equipo0participants = mutableListOf(
            match.info.participants[0],
            match.info.participants[1],
            match.info.participants[2],
            match.info.participants[3],
            match.info.participants[4]
        )
        equipo1participants = mutableListOf(
            match.info.participants[5],
            match.info.participants[6],
            match.info.participants[7],
            match.info.participants[8],
            match.info.participants[9]
        )

        binding.txtEquipo0.text = "${getString(R.string.team)} 1 - ${getWinAndKDA(equipo0participants)}"
        binding.txtEquipo1.text = "${getString(R.string.team)} 2 - ${getWinAndKDA(equipo1participants)}"

        setUpRecyclerView()

        return view
    }

    private fun getWinAndKDA(participants: MutableList<Participants>): String {
        var totalkills = 0
        var totalDeaths = 0
        var totalAsists = 0

        participants.forEach {
            totalkills += it.kills
            totalDeaths += it.deaths
            totalAsists += it.assists
        }

        val win = if (participants[0].win){ "WIN" } else { "LOSE" }

        return "$totalkills / $totalDeaths / $totalAsists - $win"
    }

    private fun setUpRecyclerView() {
        binding.rvEquipo0.setHasFixedSize(true)
        binding.rvEquipo0.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        adapter0.RecyclerAdapter(equipo0participants, requireContext().applicationContext)
        binding.rvEquipo0.adapter = adapter0

        binding.rvEquipo1.setHasFixedSize(true)
        binding.rvEquipo1.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        adapter1.RecyclerAdapter(equipo1participants, requireContext().applicationContext)
        binding.rvEquipo1.adapter = adapter1
    }
}