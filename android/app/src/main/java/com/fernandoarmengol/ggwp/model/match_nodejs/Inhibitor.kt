package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Inhibitor (

	@SerializedName("first") val first : Boolean,
	@SerializedName("kills") val kills : Int
)