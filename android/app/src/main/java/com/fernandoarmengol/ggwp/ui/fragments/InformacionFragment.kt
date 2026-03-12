package com.fernandoarmengol.ggwp.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fernandoarmengol.ggwp.GlobalVar.Companion.champCD
import com.fernandoarmengol.ggwp.GlobalVar.Companion.fixText
import com.fernandoarmengol.ggwp.R
import com.fernandoarmengol.ggwp.databinding.DialogVideoBinding
import com.fernandoarmengol.ggwp.databinding.FragmentInformacionBinding
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*


class InformacionFragment : Fragment() {
    private lateinit var binding: FragmentInformacionBinding

    private val urlHabilities = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/assets/characters/"

    @SuppressLint("SetTextI18n", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformacionBinding.inflate(inflater, container, false)
        val view = binding.root

        //Roles y dificultad
        binding.txtRole.text = "Rol: ${champCD.roles}"
        binding.txtDific.text = "${getString(R.string.difficulty)} ${resources.getStringArray(R.array.difficulty_levels)[champCD.tacticalInfo.difficulty-1]}"

        setRadarChart()
        setAbilities()

        return view
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setAbilities(){
        //Justificar texto (solo compatible con android 8 o superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtPasivaDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            binding.txtQDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            binding.txtWDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            binding.txtEDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            binding.txtRDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        //Habilidades
        val urlToRemove = "/lol-game-data/assets/ASSETS/Characters/"

        binding.txtPasiva.text = "Pasiva - ${champCD.passive.name}"
        binding.txtPasivaDesc.text = fixText(champCD.passive.description)
        loadImage(champCD.passive.abilityIconPath.replace(urlToRemove, urlHabilities).lowercase(), binding.imgPasiva)
        binding.imgPasiva.setOnClickListener {
            videoDialog(champCD.passive.abilityVideoPath, binding.txtPasiva.text as String)
        }

        binding.txtQ.text = "Q - ${champCD.spells[0].name}"
        binding.txtQDesc.text = fixText(champCD.spells[0].description)
        loadImage(champCD.spells[0].abilityIconPath.replace(urlToRemove, urlHabilities).lowercase(), binding.imgQ)
        binding.imgQ.setOnClickListener {
            videoDialog(champCD.spells[0].abilityVideoPath, binding.txtQ.text as String)
        }

        binding.txtW.text = "W - ${champCD.spells[1].name}"
        binding.txtWDesc.text = fixText(champCD.spells[1].description)
        loadImage(champCD.spells[1].abilityIconPath.replace(urlToRemove, urlHabilities).lowercase(), binding.imgW)
        binding.imgW.setOnClickListener {
            videoDialog(champCD.spells[1].abilityVideoPath, binding.txtW.text as String)
        }

        binding.txtE.text = "E - ${champCD.spells[2].name}"
        binding.txtEDesc.text = fixText(champCD.spells[2].description)
        loadImage(champCD.spells[2].abilityIconPath.replace(urlToRemove, urlHabilities).lowercase(), binding.imgE)
        binding.imgE.setOnClickListener {
            videoDialog(champCD.spells[2].abilityVideoPath, binding.txtE.text as String)
        }

        binding.txtR.text = "R - ${champCD.spells[3].name}"
        binding.txtRDesc.text = fixText(champCD.spells[3].description)
        loadImage(champCD.spells[3].abilityIconPath.replace(urlToRemove, urlHabilities).lowercase(), binding.imgR)
        binding.imgR.setOnClickListener {
            videoDialog(champCD.spells[3].abilityVideoPath, binding.txtR.text as String)
        }
    }

    private fun setRadarChart(){
        //RadarChart
        val radarEntries: MutableList<RadarEntry> = ArrayList()
        radarEntries.add(RadarEntry(champCD.playstyleInfo.damage.toFloat()))
        radarEntries.add(RadarEntry(champCD.playstyleInfo.durability.toFloat()))
        radarEntries.add(RadarEntry(champCD.playstyleInfo.crowdControl.toFloat()))
        radarEntries.add(RadarEntry(champCD.playstyleInfo.mobility.toFloat()))
        radarEntries.add(RadarEntry(champCD.playstyleInfo.utility.toFloat()))

        val xAxis = binding.radarChart.xAxis
        xAxis.textColor = Color.GRAY
        xAxis.valueFormatter = IndexAxisValueFormatter(resources.getStringArray(R.array.type))

        val yAxis = binding.radarChart.yAxis
        yAxis.setDrawLabels(false)
        yAxis.axisMinimum = 0f

        val radarData = RadarData()
        val radarDataSet = RadarDataSet(radarEntries, "")
        radarDataSet.color = Color.BLUE
        radarDataSet.valueTextColor = Color.GRAY
        radarData.addDataSet(radarDataSet)

        binding.radarChart.description.isEnabled = false
        binding.radarChart.legend.isEnabled = false
        binding.radarChart.data = radarData
    }

    private fun videoDialog(url: String, name: String){
        val builder = AlertDialog.Builder(context)

        builder.apply {
            val bindingDialogLayout: DialogVideoBinding = DialogVideoBinding.inflate(layoutInflater)
            setView(bindingDialogLayout.root)

            bindingDialogLayout.txtName.text = name

            val mediaController = MediaController(context)
            mediaController.setAnchorView(bindingDialogLayout.videoView)
            mediaController.setMediaPlayer(bindingDialogLayout.videoView)

            val videoUrl = "https://d28xe8vt774jo5.cloudfront.net/$url"
            val uri: Uri = Uri.parse(videoUrl)
            bindingDialogLayout.videoView.setVideoURI(uri)
            bindingDialogLayout.videoView.start()
            bindingDialogLayout.videoView.setOnPreparedListener{
                bindingDialogLayout.progressBar.visibility = View.GONE
            }

            setNeutralButton(getString(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }
}