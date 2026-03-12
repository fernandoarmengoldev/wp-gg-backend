package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Teams (

	@SerializedName("bans") val bans : List<Bans>,
	@SerializedName("objectives") val objectives : Objectives,
	@SerializedName("teamId") val teamId : Int,
	@SerializedName("win") val win : Boolean
)