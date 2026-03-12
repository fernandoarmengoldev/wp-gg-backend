package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class Coefficients (

	@SerializedName("coefficient1") val coefficient1 : Double,
	@SerializedName("coefficient2") val coefficient2 : Double
)