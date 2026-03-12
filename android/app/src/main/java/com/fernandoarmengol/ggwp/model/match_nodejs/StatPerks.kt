package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class StatPerks (

	@SerializedName("defense") val defense : Int,
	@SerializedName("flex") val flex : Int,
	@SerializedName("offense") val offense : Int
)