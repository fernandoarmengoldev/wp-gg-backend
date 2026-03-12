package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class PlaystyleInfo (

	@SerializedName("damage") val damage : Int,
	@SerializedName("durability") val durability : Int,
	@SerializedName("crowdControl") val crowdControl : Int,
	@SerializedName("mobility") val mobility : Int,
	@SerializedName("utility") val utility : Int
)