package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Metadata (

	@SerializedName("dataVersion") val dataVersion : Int,
	@SerializedName("matchId") val matchId : String,
	@SerializedName("participants") val participants : List<String>
)