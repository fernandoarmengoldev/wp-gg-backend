package com.fernandoarmengol.ggwp.model.champ_stadistics_nodejs

import com.google.gson.annotations.SerializedName

data class Runes (

	@SerializedName("value") val value : String,
	@SerializedName("porcentaje") val porcentaje : Double
)