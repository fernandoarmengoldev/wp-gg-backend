package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class SpellsCD (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("description") val description : String,
	@SerializedName("summonerLevel") val summonerLevel : Int,
	@SerializedName("cooldown") val cooldown : Int,
	@SerializedName("gameModes") val gameModes : List<String>,
	@SerializedName("iconPath") val iconPath : String
)