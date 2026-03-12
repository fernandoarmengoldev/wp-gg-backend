package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Bans (

	@SerializedName("championId") val championId : Int,
	@SerializedName("pickTurn") val pickTurn : Int
)