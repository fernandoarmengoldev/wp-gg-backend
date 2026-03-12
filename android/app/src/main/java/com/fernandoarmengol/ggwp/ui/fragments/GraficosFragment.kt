package com.fernandoarmengol.ggwp.ui.fragments

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.fernandoarmengol.ggwp.GlobalVar.Companion.match
import com.fernandoarmengol.ggwp.databinding.FragmentGraficosBinding
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.reflect.full.memberProperties


class GraficosFragment : Fragment() {
    lateinit var binding: FragmentGraficosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraficosBinding.inflate(inflater, container, false)
        val view = binding.root

        val urlChamps = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/"
        with(binding){
            val imgViews = arrayListOf(ivImg0, ivImg1, ivImg2, ivImg3, ivImg4, ivImg5, ivImg6, ivImg7, ivImg8, ivImg9)
            imgViews.forEachIndexed { index, imageView ->
                loadImage(urlChamps + match.info.participants[index].championId + ".png" , imageView)
            }
        }

        //Adaptador para el sppinner
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(com.fernandoarmengol.ggwp.R.array.charts)
        )

        val filtros = arrayListOf(
            "totalDamageDealtToChampions",
            "physicalDamageDealtToChampions",
            "magicDamageDealtToChampions",
            "trueDamageDealtToChampions",
            "physicalDamageDealt",
            "magicDamageDealt",
            "trueDamageDealt",
            "damageDealtToTurrets",
            "damageDealtToObjectives",
            "totalHeal",
            "totalDamageTaken",
            "physicalDamageTaken",
            "magicDamageTaken",
            "trueDamageTaken",
            "damageSelfMitigated",
            "goldEarned",
            "goldSpent",
            "visionScore",
            "wardsPlaced",
            "wardsKilled",
            "sightWardsBoughtInGame",
            "totalMinionsKilled",
        )

        binding.spinner2.adapter = adapter
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setData(filtros[binding.spinner2.selectedItemPosition])
            }
        }
        setData("totalDamageDealtToChampions")

        return view
    }

    @Throws(IllegalAccessException::class, ClassCastException::class)
    inline fun <reified T> Any.getField(fieldName: String): T? {
        this::class.memberProperties.forEach { kCallable ->
            if (fieldName == kCallable.name) {
                return kCallable.getter.call(this) as T?
            }
        }
        return null
    }

    private fun setData(variable: String) {
        val barEntries: MutableList<BarEntry> = ArrayList()

        match.info.participants.asReversed().forEachIndexed { index, participant ->
            barEntries.add(BarEntry(index * 10f, participant.getField<Int>(variable)!!.toFloat()))
        }

        val barData = BarData()
        barData.barWidth = 5f

        val barDataSet = BarDataSet(barEntries, "Data")
        barDataSet.valueTextColor = Color.GRAY
        barDataSet.valueTextSize = 10f

        val yl: YAxis = binding.barChart.axisLeft
        yl.textColor = Color.GRAY
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f

        val xl: XAxis = binding.barChart.xAxis
        xl.position = XAxisPosition.TOP
        xl.setDrawLabels(false)
        xl.setDrawAxisLine(false)
        xl.setDrawGridLines(false)
        xl.granularity = 10f

        val yr: YAxis = binding.barChart.axisRight
        yr.textColor = Color.GRAY
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(true)
        yr.axisMinimum = 0f

        binding.barChart.setFitBars(true)

        barData.addDataSet(barDataSet)

        binding.barChart.setDrawValueAboveBar(true)

        binding.barChart.description.isEnabled = false
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.legend.isEnabled = false

        binding.barChart.data = barData
        binding.barChart.invalidate()
    }
}