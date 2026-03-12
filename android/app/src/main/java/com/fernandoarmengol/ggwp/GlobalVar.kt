package com.fernandoarmengol.ggwp

import android.app.Application
import com.fernandoarmengol.ggwp.model.ChampionSummaryCD
import com.fernandoarmengol.ggwp.model.ItemsCD
import com.fernandoarmengol.ggwp.model.PerksCD
import com.fernandoarmengol.ggwp.model.SpellsCD
import com.fernandoarmengol.ggwp.model.champ_cd.ChampCD
import com.fernandoarmengol.ggwp.model.champ_stadistics_nodejs.ChampStadisticsNodeJS
import com.fernandoarmengol.ggwp.model.match_nodejs.MatchNodeJS
import com.fernandoarmengol.ggwp.model.perk_styles_cd.PerkStyleCD

class GlobalVar : Application() {
    companion object {
        var championSummaryCD: MutableList<ChampionSummaryCD> = ArrayList()
        var itemsCD: MutableList<ItemsCD> = ArrayList()
        var spellsCD: MutableList<SpellsCD> = ArrayList()
        var perksCD: MutableList<PerksCD> = ArrayList()
        lateinit var perkStyleCD: PerkStyleCD

        lateinit var champStadisticsNodeJS: ChampStadisticsNodeJS
        lateinit var champCD: ChampCD

        lateinit var match: MatchNodeJS

        //Metodo para eliminar etiquetas html
        fun fixText(string: String): String {
            return string
                .replace("</stats>", "\n")
                .replace("<br><br> ", "\n")
                .replace("<br>", "\n")
                .replace(Regex("(<.+?>)"), "")
        }
    }
}