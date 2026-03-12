package com.fernandoarmengol.ggwp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fernandoarmengol.ggwp.GlobalVar.Companion.spellsCD
import com.fernandoarmengol.ggwp.databinding.ActivitySpellsBinding
import com.fernandoarmengol.ggwp.model.SpellsCD
import com.fernandoarmengol.ggwp.ui.adapters.RVSpellsAdapter

class SpellsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySpellsBinding
    private val adapter: RVSpellsAdapter = RVSpellsAdapter()

    private val spellsFiltered = mutableListOf<SpellsCD>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpellsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (spell in spellsCD) {
            if (spell.name.isNotEmpty() &&
                (spell.gameModes.contains("ARAM") || spell.gameModes.contains("CLASSIC"))){
                spellsFiltered.add(spell)
            }
        }

        setUpRecyclerView()
    }

    // Método encargado de configurar el RV.
    private fun setUpRecyclerView() {
        binding.rvSpells.setHasFixedSize(true)
        binding.rvSpells.layoutManager = GridLayoutManager(this.applicationContext, 5)
        adapter.RecyclerAdapter(spellsFiltered, this)
        binding.rvSpells.adapter = adapter
    }
}