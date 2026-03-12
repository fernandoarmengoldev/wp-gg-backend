package com.fernandoarmengol.ggwp.model.perk_styles_cd

import com.google.gson.annotations.SerializedName

data class DefaultStatModsPerSubStyle (

	@SerializedName("id") val id : Int,
	@SerializedName("perks") val perks : List<Int>
)