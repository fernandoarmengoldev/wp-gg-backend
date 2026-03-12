package com.fernandoarmengol.ggwp.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fernandoarmengol.ggwp.GlobalVar.Companion.perksCD
import com.fernandoarmengol.ggwp.databinding.ActivityRunesBinding
import com.fernandoarmengol.ggwp.dialogs.DialogSpellAndRune
import com.fernandoarmengol.ggwp.network.GlideHelper.loadImage

class RunesActivity : AppCompatActivity() {
    lateinit var binding: ActivityRunesBinding

    private var runeFilter = 0
    lateinit var data: MutableList<Int>

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runeFilter = 0
        loadData()

        binding.toggleButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.btnPre.id -> runeFilter = 0
                    binding.btnDom.id -> runeFilter = 1
                    binding.btnBru.id -> runeFilter = 2
                    binding.btnRes.id -> runeFilter = 3
                    binding.btnIns.id -> runeFilter = 4
                }
            }
            loadData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadData(){
        when (runeFilter) {
            0 -> data = mutableListOf(8005, 8008, 8021, 8010, 9101, 9111, 8009, 9104, 9105, 9103, 8014, 8017, 8299, 0)
            1 -> data = mutableListOf(8112, 8124, 8128, 9923, 8126, 8139, 8143, 8136, 8120, 8138, 8135, 8134, 8105, 8106)
            2 -> data = mutableListOf(8214, 8229, 8230, 0, 8224, 8226, 8275, 8210, 8234, 8233, 8237, 8232, 8236, 0)
            3 -> data = mutableListOf(8437, 8439, 8465, 0, 8446, 8463, 8401, 8429, 8444, 8473, 8451, 8453, 8242, 0)
            4 -> data = mutableListOf(8351, 8360, 8369, 0, 8306, 8304, 8313, 8321, 8316, 8345, 8347, 8410, 8352, 0)
        }

        val urlRunes = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/perk-images/"
        val urlToRemove = "/lol-game-data/assets/v1/perk-images/"

        val runesA = mutableListOf(binding.rune0, binding.rune1, binding.rune2, binding.rune3)
        val runesB = mutableListOf(binding.rune4, binding.rune5, binding.rune6, binding.rune7, binding.rune8, binding.rune9, binding.rune10, binding.rune11, binding.rune12, binding.rune13)

        var index = 0
        //Fase uno de carga de runas
        for (view in runesA) {
            if (data[index] != 0) {
                val perk = perksCD.find { perk -> perk.id == data[index] }!!

                view.cvItem.visibility = View.VISIBLE

                val urlRune = urlRunes + perk.iconPath.replace(urlToRemove, "").lowercase()
                loadImage(urlRune, view.ivImg)

                view.ivImg.setOnClickListener {
                    DialogSpellAndRune.dialogSpellAndRune(this, perk.name, perk.longDesc, urlRune)
                }
            } else {
                view.cvItem.visibility = View.GONE
            }
            index++
        }
        //Fase dos de carga de runas
        for (view in runesB) {
            if (data[index] != 0) {
                val perk = perksCD.find { perk -> perk.id == data[index] }!!

                view.cvItem.visibility = View.VISIBLE

                val urlRune = urlRunes + perk.iconPath.replace(urlToRemove, "").lowercase()
                loadImage(urlRune, view.ivImg)

                view.ivImg.setOnClickListener {
                    DialogSpellAndRune.dialogSpellAndRune(this, perk.name, perk.longDesc, urlRune)
                }
            } else {
                view.cvItem.visibility = View.GONE
            }
            index++
        }
    }
}