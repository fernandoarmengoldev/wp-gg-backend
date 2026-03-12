package com.fernandoarmengol.ggwp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fernandoarmengol.ggwp.databinding.FragmentSummonerBinding
import com.fernandoarmengol.ggwp.ui.activities.SummonerActivity
import com.fernandoarmengol.ggwp.ui.adapters.RVSumonerAdapter

class SummonerFragment : Fragment() {
    private lateinit var binding: FragmentSummonerBinding
    private val adapter: RVSumonerAdapter = RVSumonerAdapter()

    private lateinit var prefs: SharedPreferences
    private var names = mutableSetOf<String>()

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSummonerBinding.inflate(inflater, container, false)
        val view = binding.root

        prefs = context?.getSharedPreferences("SharedPrefs", MODE_PRIVATE)!!
        names = prefs.getStringSet("names", mutableSetOf<String>())!!
        setUpRecyclerView()

        binding.entryName.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                buscar()
                true
            } else false
        }
        binding.btnSearch.setOnClickListener{
            buscar()
        }

        return view
    }

    @SuppressLint("CommitPrefEdits", "NotifyDataSetChanged")
    private fun saveName(name: String){
        names.add(name)

        adapter.RecyclerAdapter(names.toMutableList(), requireContext().applicationContext)
        adapter.notifyDataSetChanged()

        prefs.edit().clear().apply()
        prefs.edit()?.putStringSet("names", names)?.apply()
    }

    private fun buscar(){
        if (!binding.entryName.text.isNullOrBlank()) {
            saveName(binding.entryName.text.toString())

            val extras = Bundle()
            extras.putString("EXTRA_SUMMONER", binding.entryName.text.toString())

            binding.entryName.text.clear()

            val intent = Intent(context, SummonerActivity::class.java).apply {
                putExtras(extras)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
    }

    // Método encargado de configurar el RV.
    private fun setUpRecyclerView() {
        binding.rvRecent.setHasFixedSize(true)
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        adapter.RecyclerAdapter(names.toMutableList(), requireContext().applicationContext)
        binding.rvRecent.adapter = adapter
    }
}