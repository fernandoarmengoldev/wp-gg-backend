package com.fernandoarmengol.ggwp.model.perk_styles_cd

import com.google.gson.annotations.SerializedName

data class Slots (

	@SerializedName("type") val type : String,
	@SerializedName("slotLabel") val slotLabel : String,
	@SerializedName("perks") val perks : List<Int>
)