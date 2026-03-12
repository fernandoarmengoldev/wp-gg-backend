package com.fernandoarmengol.ggwp.model.perk_styles_cd

import com.google.gson.annotations.SerializedName

data class SubStyleBonus (

	@SerializedName("styleId") val styleId : Int,
	@SerializedName("perkId") val perkId : Int
)