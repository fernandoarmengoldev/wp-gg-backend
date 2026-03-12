package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class EloNodeJS (

	@SerializedName("leagueId") val leagueId : String,
	@SerializedName("queueType") val queueType : String,
	@SerializedName("tier") val tier : String,
	@SerializedName("rank") val rank : String,
	@SerializedName("summonerId") val summonerId : String,
	@SerializedName("summonerName") val summonerName : String,
	@SerializedName("leaguePoints") val leaguePoints : Int,
	@SerializedName("wins") val wins : Int,
	@SerializedName("losses") val losses : Int,
	@SerializedName("veteran") val veteran : Boolean,
	@SerializedName("inactive") val inactive : Boolean,
	@SerializedName("freshBlood") val freshBlood : Boolean,
	@SerializedName("hotStreak") val hotStreak : Boolean
)