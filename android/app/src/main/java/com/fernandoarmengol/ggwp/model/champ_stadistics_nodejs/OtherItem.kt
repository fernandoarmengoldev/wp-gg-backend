package com.fernandoarmengol.ggwp.model.champ_stadistics_nodejs

import com.google.gson.annotations.SerializedName

data class OtherItem (

	@SerializedName("value") val value : Int,
	@SerializedName("porcentaje") val porcentaje : Double
)