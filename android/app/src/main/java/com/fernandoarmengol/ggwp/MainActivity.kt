package com.fernandoarmengol.ggwp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fernandoarmengol.ggwp.GlobalVar.Companion.championSummaryCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.itemsCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perkStyleCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perksCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.spellsCD
import com.fernandoarmengol.ggwp.databinding.ActivityMainBinding
import com.fernandoarmengol.ggwp.dialogs.NetworkError
import com.fernandoarmengol.ggwp.model.ChampionSummaryCD
import com.fernandoarmengol.ggwp.model.ItemsCD
import com.fernandoarmengol.ggwp.model.PerksCD
import com.fernandoarmengol.ggwp.model.SpellsCD
import com.fernandoarmengol.ggwp.network.APIService
import com.fernandoarmengol.ggwp.network.RetrofitHelper
import com.fernandoarmengol.ggwp.ui.adapters.ViewPager2Adapter
import com.fernandoarmengol.ggwp.ui.fragments.ChampionsFragment
import com.fernandoarmengol.ggwp.ui.fragments.SummonerFragment
import com.fernandoarmengol.ggwp.ui.fragments.WikiFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se crea el adapter.
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)

        adapter.addFragment(SummonerFragment(), getString(R.string.summoner))
        adapter.addFragment(ChampionsFragment(), getString(R.string.champions))
        adapter.addFragment(WikiFragment(), getString(R.string.wiki))

        // Se asocia el adapter al ViewPager2.
        binding.viewPager2.adapter = adapter

        // Carga de las pestañas en el TabLayout.
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callChampionSummaryCD =
                    RetrofitHelper.getRetrofitComunityDragon(getString(R.string.url_main_cd))
                        .create(APIService::class.java).getChampSumaryCD()
                val callItemsCD =
                    RetrofitHelper.getRetrofitComunityDragon(getString(R.string.url_main_cd))
                        .create(APIService::class.java).getItemsCD()
                val callSpellsCD =
                    RetrofitHelper.getRetrofitComunityDragon(getString(R.string.url_main_cd))
                        .create(APIService::class.java).getSpellsCD()
                val callPerksCD =
                    RetrofitHelper.getRetrofitComunityDragon(getString(R.string.url_main_cd))
                        .create(APIService::class.java).getPerksCD()
                val callPerkStylesCD =
                    RetrofitHelper.getRetrofitComunityDragon(getString(R.string.url_main_cd))
                        .create(APIService::class.java).getPerkStylesCD()
                if (
                    callChampionSummaryCD.isSuccessful &&
                    callItemsCD.isSuccessful &&
                    callSpellsCD.isSuccessful &&
                    callPerksCD.isSuccessful &&
                    callPerkStylesCD.isSuccessful
                ) {
                    championSummaryCD =
                        (callChampionSummaryCD.body() as MutableList<ChampionSummaryCD>?)!!
                    itemsCD = (callItemsCD.body() as MutableList<ItemsCD>?)!!
                    spellsCD = (callSpellsCD.body() as MutableList<SpellsCD>?)!!
                    perksCD = (callPerksCD.body() as MutableList<PerksCD>?)!!
                    perkStyleCD = callPerkStylesCD.body()!!
                }
            } catch (e: Exception) {
                runOnUiThread {
                    NetworkError.dialogNetworkError(
                        context = this@MainActivity,
                        getString(R.string.network_error)
                    )
                }
            }
        }
    }
}