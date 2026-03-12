package com.fernandoarmengol.ggwp.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fernandoarmengol.ggwp.GlobalVar.Companion.champCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.champStadisticsNodeJS
import com.fernandoarmengol.ggwp.GlobalVar.Companion.championSummaryCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.itemsCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perkStyleCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perksCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.spellsCD
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.FragmentEstadisticasBinding
import com.fernandoarmengol.ggwp.dialogs.DialogItem
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import com.fernandoarmengol.ggwp.ui.activities.ChampActivity

class EstadisticasFragment : Fragment() {
    private lateinit var binding: FragmentEstadisticasBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        val view = binding.root

        //Runas
        setRunes(0)
        binding.toggleButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.button.id -> setRunes(0)
                    binding.button2.id -> setRunes(1)
                }
            }
        }

        setGoodAgainst()
        setBadAgainst()
        setSpells()
        setMythics()
        setLegendary()
        setBoots()

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setGoodAgainst() {
        val bestFiveSorted = champStadisticsNodeJS.bestFive.sortedWith(compareByDescending { it.value })

        binding.txtFuerteContra.text = "${champCD.name} ${getString(R.string.good_against)}"

        binding.fuerte0.txtName.text = championSummaryCD.find{ champ -> champ.id == bestFiveSorted[0].id }!!.name
        binding.fuerte1.txtName.text = championSummaryCD.find{ champ -> champ.id == bestFiveSorted[1].id }!!.name
        binding.fuerte2.txtName.text = championSummaryCD.find{ champ -> champ.id == bestFiveSorted[2].id }!!.name
        binding.fuerte3.txtName.text = championSummaryCD.find{ champ -> champ.id == bestFiveSorted[3].id }!!.name

        binding.txtFuertePer0.text = "${bestFiveSorted[0].value}%"
        binding.txtFuertePer1.text = "${bestFiveSorted[1].value}%"
        binding.txtFuertePer2.text = "${bestFiveSorted[2].value}%"
        binding.txtFuertePer3.text = "${bestFiveSorted[3].value}%"

        loadImage(getString(R.string.url_champ_icon) + bestFiveSorted[0].id + ".png", binding.fuerte0.ivImg)
        loadImage(getString(R.string.url_champ_icon) + bestFiveSorted[1].id + ".png", binding.fuerte1.ivImg)
        loadImage(getString(R.string.url_champ_icon) + bestFiveSorted[2].id + ".png", binding.fuerte2.ivImg)
        loadImage(getString(R.string.url_champ_icon) + bestFiveSorted[3].id + ".png", binding.fuerte3.ivImg)

        binding.fuerte0.ivImg.setOnClickListener { champInfo(bestFiveSorted[0].id) }
        binding.fuerte1.ivImg.setOnClickListener { champInfo(bestFiveSorted[1].id) }
        binding.fuerte2.ivImg.setOnClickListener { champInfo(bestFiveSorted[2].id) }
        binding.fuerte3.ivImg.setOnClickListener { champInfo(bestFiveSorted[3].id) }
    }

    @SuppressLint("SetTextI18n")
    private fun setBadAgainst() {
        val worstFiveSorted = champStadisticsNodeJS.worstFive.sortedWith(compareBy { it.value })

        binding.txtDebilContra.text = "${champCD.name} ${getString(R.string.bad_against)}"

        binding.debil0.txtName.text = championSummaryCD.find{ champ -> champ.id == worstFiveSorted[0].id }!!.name
        binding.debil1.txtName.text = championSummaryCD.find{ champ -> champ.id == worstFiveSorted[1].id }!!.name
        binding.debil2.txtName.text = championSummaryCD.find{ champ -> champ.id == worstFiveSorted[2].id }!!.name
        binding.debil3.txtName.text = championSummaryCD.find{ champ -> champ.id == worstFiveSorted[3].id }!!.name

        binding.txtDebilPer0.text = "${worstFiveSorted[0].value}%"
        binding.txtDebilPer1.text = "${worstFiveSorted[1].value}%"
        binding.txtDebilPer2.text = "${worstFiveSorted[2].value}%"
        binding.txtDebilPer3.text = "${worstFiveSorted[3].value}%"

        loadImage(getString(R.string.url_champ_icon) + worstFiveSorted[0].id + ".png", binding.debil0.ivImg)
        loadImage(getString(R.string.url_champ_icon) + worstFiveSorted[1].id + ".png", binding.debil1.ivImg)
        loadImage(getString(R.string.url_champ_icon) + worstFiveSorted[2].id + ".png", binding.debil2.ivImg)
        loadImage(getString(R.string.url_champ_icon) + worstFiveSorted[3].id + ".png", binding.debil3.ivImg)

        binding.debil0.ivImg.setOnClickListener { champInfo(worstFiveSorted[0].id) }
        binding.debil1.ivImg.setOnClickListener { champInfo(worstFiveSorted[1].id) }
        binding.debil2.ivImg.setOnClickListener { champInfo(worstFiveSorted[2].id) }
        binding.debil3.ivImg.setOnClickListener { champInfo(worstFiveSorted[3].id) }
    }

    @SuppressLint("SetTextI18n")
    private fun setSpells() {
        val hechizo0A = spellsCD.find{ spell -> spell.id == champStadisticsNodeJS.spells[0].value }!!
        val hechizo0B = spellsCD.find{ spell -> spell.id == champStadisticsNodeJS.spells[1].value }!!
        val hechizo1B = spellsCD.find{ spell -> spell.id == champStadisticsNodeJS.spells[2].value }!!

        loadImage(getString(R.string.url_spells_icon) + hechizo0A.iconPath.split("/").last().lowercase(), binding.imgHechizo1A)
        loadImage(getString(R.string.url_spells_icon) + hechizo0B.iconPath.split("/").last().lowercase(), binding.imgHechizo1B)
        loadImage(getString(R.string.url_spells_icon) + hechizo0A.iconPath.split("/").last().lowercase(), binding.imgHechizo2A)
        loadImage(getString(R.string.url_spells_icon) + hechizo1B.iconPath.split("/").last().lowercase(), binding.imgHechizo2B)

        binding.txtHechizo1.text = "${hechizo0A.name} / ${hechizo0B.name}"
        binding.txtHechizo2.text = "${hechizo0A.name} / ${hechizo1B.name}"

        binding.txtHechizoPick1.text = "PickRate: ${champStadisticsNodeJS.spells[1].porcentaje}%"
        binding.txtHechizoPick2.text = "PickRate: ${champStadisticsNodeJS.spells[2].porcentaje}%"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun setMythics() {
        val mitico0 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.mythics[0].value }!!
        val mitico1 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.mythics[1].value }!!

        loadImage(getString(R.string.url_items_icon) + mitico0.iconPath.split("/").last().lowercase(), binding.imgMitico1)
        loadImage(getString(R.string.url_items_icon) + mitico1.iconPath.split("/").last().lowercase(), binding.imgMitico2)

        binding.imgMitico1.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, mitico0) }
        binding.imgMitico2.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, mitico1) }

        binding.txtMitico1.text = mitico0.name
        binding.txtMitico2.text = mitico1.name

        binding.txtMiticoPick1.text = "PickRate: ${champStadisticsNodeJS.mythics[0].porcentaje}%"
        binding.txtMiticoPick2.text = "PickRate: ${champStadisticsNodeJS.mythics[1].porcentaje}%"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun setLegendary() {
        val legendarios0 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.otherItem[0].value }!!
        val legendarios1 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.otherItem[1].value }!!
        val legendarios2 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.otherItem[2].value }!!

        loadImage(getString(R.string.url_items_icon) + legendarios0.iconPath.split("/").last().lowercase(), binding.imgLegendario1)
        loadImage(getString(R.string.url_items_icon) + legendarios1.iconPath.split("/").last().lowercase(), binding.imgLegendario2)
        loadImage(getString(R.string.url_items_icon) + legendarios2.iconPath.split("/").last().lowercase(), binding.imgLegendario3)

        binding.imgLegendario1.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, legendarios0) }
        binding.imgLegendario2.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, legendarios1) }
        binding.imgLegendario3.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, legendarios2) }

        binding.txtLegendario1.text = legendarios0.name
        binding.txtLegendario2.text = legendarios1.name
        binding.txtLegendario3.text = legendarios2.name

        binding.txtLegendarioPick1.text = "PickRate: ${champStadisticsNodeJS.otherItem[0].porcentaje}%"
        binding.txtLegendarioPick2.text = "PickRate: ${champStadisticsNodeJS.otherItem[1].porcentaje}%"
        binding.txtLegendarioPick3.text = "PickRate: ${champStadisticsNodeJS.otherItem[2].porcentaje}%"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun setBoots() {
        val botas0 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.boots[0].value }!!
        val botas1 = itemsCD.find{ item -> item.id == champStadisticsNodeJS.boots[1].value }!!

        loadImage(getString(R.string.url_items_icon) + botas0.iconPath.split("/").last().lowercase(), binding.imgBotas1)
        loadImage(getString(R.string.url_items_icon) + botas1.iconPath.split("/").last().lowercase(), binding.imgBotas2)

        binding.imgBotas1.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, botas0) }
        binding.imgBotas2.setOnClickListener { DialogItem.dialogItem(requireContext(), itemsCD, botas1) }

        binding.txtBotas1.text = botas0.name
        binding.txtBotas2.text = botas1.name

        binding.txtBotasPick1.text = "PickRate: ${champStadisticsNodeJS.boots[0].porcentaje}%"
        binding.txtBotasPick2.text = "PickRate: ${champStadisticsNodeJS.boots[1].porcentaje}%"
    }

    @SuppressLint("SetTextI18n")
    private fun setRunes(index: Int){
        val urlToRemove = "/lol-game-data/assets/v1/perk-images/"
        binding.txtRunePickRate.text = "${champStadisticsNodeJS.runes[index].porcentaje}%"

        //Runas Primarias
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[7].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna0)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[8].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna1)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[9].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna2)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[10].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna3)

        //Runas Secundarias
        loadImage(getString(R.string.url_runes_icon) + perkStyleCD.styles.find { style -> style.id == champStadisticsNodeJS.runes[index].value.split(",")[11].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna4)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[12].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna5)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[13].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna6)

        //Runas Terciarias
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[5].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna7)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[3].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna8)
        loadImage(getString(R.string.url_runes_icon) + perksCD.find { perk -> perk.id == champStadisticsNodeJS.runes[index].value.split(",")[1].toInt() }?.iconPath?.replace(urlToRemove, "")?.lowercase(), binding.imgRuna9)
    }

    private fun champInfo(id: Int){
        val extras = Bundle()
        extras.putInt("EXTRA_CHAMP", id)

        val intent = Intent(activity, ChampActivity::class.java).apply {
            putExtras(extras)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}