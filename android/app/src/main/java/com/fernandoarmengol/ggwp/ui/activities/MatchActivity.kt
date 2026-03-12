package com.fernandoarmengol.ggwp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fernandoarmengol.ggwp.databinding.ActivityItemsBinding
import com.fernandoarmengol.ggwp.databinding.ActivityMatchBinding
import com.fernandoarmengol.ggwp.ui.adapters.ViewPager2Adapter
import com.fernandoarmengol.ggwp.ui.fragments.*
import com.google.android.material.tabs.TabLayoutMediator

class MatchActivity : AppCompatActivity() {
    lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se crea el adapter.
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)

        adapter.addFragment(GeneralFragment(), "General")
        adapter.addFragment(GraficosFragment(), "Graficos")

        // Se asocia el adapter al ViewPager2.
        binding.viewPager2.adapter = adapter

        // Carga de las pestañas en el TabLayout.
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }
}