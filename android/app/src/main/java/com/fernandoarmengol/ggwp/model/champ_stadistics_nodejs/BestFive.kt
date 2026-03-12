package com.fernandoarmengol.ggwp.model.champ_stadistics_nodejs

import com.google.gson.annotations.SerializedName

data class BestFive (

	@SerializedName("id") val id : Int,
	@SerializedName("value") val value : Double
)