package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class TacticalInfo (

	@SerializedName("style") val style : Int,
	@SerializedName("difficulty") val difficulty : Int,
	@SerializedName("damageType") val damageType : String
)