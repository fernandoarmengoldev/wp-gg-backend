package com.fernandoarmengol.ggwp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.fernandoarmengol.ggwp.GlobalVar.Companion.itemsCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perkStyleCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perksCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.spellsCD
import com.fernandoarmengol.ggwp.databinding.ItemMatchBinding
import com.fernandoarmengol.ggwp.model.match_nodejs.Participants
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import com.fernandoarmengol.ggwp.ui.activities.ChampActivity
import com.fernandoarmengol.ggwp.ui.activities.SummonerActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RVParticipantsAdapter : RecyclerView.Adapter<RVParticipantsAdapter.ViewHolder>() {

    var participants: MutableList<Participants> = ArrayList()

    lateinit var context: Context
    private lateinit var puuid: String

    // Constructor de la clase. Se pasa la fuente de datos y el contexto sobre el que se mostrará.
    fun RecyclerAdapter(participants: MutableList<Participants>, contxt: Context) {
        this.participants = participants
        this.context = contxt
    }

    // Este método se encarga de pasar los objetos, uno a uno al ViewHolder personalizado.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = participants.get(position)
        holder.bind(item, context)
    }

    // Es el encargado de devolver el ViewHolder ya configurado.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemMatchBinding.inflate(
                layoutInflater,
                parent,
                false
            ).root
        )
    }

    // Devuelve el tamaño del array.
    override fun getItemCount(): Int {
        return participants.size
    }

    // Esta clase se encarga de rellenar cada una de las vistas que se inflarán en el RecyclerView.
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Se usa View Binding para localizar los elementos en la vista.
        private val binding = ItemMatchBinding.bind(view)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(participant: Participants, context: Context) {

            binding.txtKda.text = "${participant.kills} / ${participant.deaths} / ${participant.assists}"
            binding.txtCS.text = "CS: ${participant.totalMinionsKilled}"
            binding.txtType.text = "${participant.summonerName}      "

            val urlItems = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/assets/items/icons2d/"
            val urlSpells = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/data/spells/icons2d/"
            val urlChamps = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/"
            val urlRunes = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/perk-images/"

            //Champ
            loadImage(urlChamps + participant.championId + ".png", binding.ivImg)

            //Items
            val itemsId = mutableListOf(participant.item0, participant.item1, participant.item2, participant.item3, participant.item4, participant.item5)
            val itemsViews = mutableListOf(binding.imgItem0, binding.imgItem1, binding.imgItem2, binding.imgItem3, binding.imgItem4, binding.imgItem5)
            itemsId.forEachIndexed { i, item ->
                if (item != 0) {
                    loadImage(urlItems + itemsCD.find { itemCD -> itemCD.id == item }!!.iconPath.split("/").last().lowercase(), itemsViews[i])
                }
            }

            //Runas
            val urlToRemove = "/lol-game-data/assets/v1/perk-images/"
            loadImage(urlRunes + perksCD.find { perk -> perk.id == participant.perks.styles[0].selections[0].perk }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRune0)
            loadImage(urlRunes + perkStyleCD.styles.find { perk -> perk.id == participant.perks.styles[1].style }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRune1)

            //Spells
            loadImage(urlSpells + spellsCD.find { spell -> spell.id == participant.summoner1Id }!!.iconPath.split("/").last().lowercase(), binding.imgSpell0)
            loadImage(urlSpells + spellsCD.find { spell -> spell.id == participant.summoner2Id }!!.iconPath.split("/").last().lowercase(), binding.imgSpell1)

            binding.mainLayout.setOnClickListener {
                val extras = Bundle()
                extras.putString("EXTRA_SUMMONER", participant.summonerName)

                val intent = Intent(context, SummonerActivity::class.java).apply {
                    putExtras(extras)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }
}