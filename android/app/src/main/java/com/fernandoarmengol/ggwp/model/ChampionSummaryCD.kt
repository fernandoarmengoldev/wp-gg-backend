package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class ChampionSummaryCD (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("alias") val alias : String,
	@SerializedName("squarePortraitPath") val squarePortraitPath : String,
	@SerializedName("roles") val roles : List<String>
)