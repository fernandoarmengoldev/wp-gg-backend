package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class Spells (

    @SerializedName("spellKey") val spellKey : String,
    @SerializedName("name") val name : String,
    @SerializedName("abilityIconPath") val abilityIconPath : String,
    @SerializedName("abilityVideoPath") val abilityVideoPath : String,
    @SerializedName("abilityVideoImagePath") val abilityVideoImagePath : String,
    @SerializedName("cost") val cost : String,
    @SerializedName("cooldown") val cooldown : String,
    @SerializedName("description") val description : String,
    @SerializedName("dynamicDescription") val dynamicDescription : String,
    @SerializedName("range") val range : List<Double>,
    @SerializedName("costCoefficients") val costCoefficients : List<Double>,
    @SerializedName("cooldownCoefficients") val cooldownCoefficients : List<Double>,
    @SerializedName("coefficients") val coefficients : Coefficients,
    @SerializedName("effectAmounts") val effectAmounts : EffectAmounts,
    @SerializedName("ammo") val ammo : Ammo,
    @SerializedName("maxLevel") val maxLevel : Int
)