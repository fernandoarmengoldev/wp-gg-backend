package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Info (

	@SerializedName("gameCreation") val gameCreation : Long,
	@SerializedName("gameDuration") val gameDuration : Long,
	@SerializedName("gameEndTimestamp") val gameEndTimestamp : Long,
	@SerializedName("gameId") val gameId : Long,
	@SerializedName("gameMode") val gameMode : String,
	@SerializedName("gameName") val gameName : String,
	@SerializedName("gameStartTimestamp") val gameStartTimestamp : Long,
	@SerializedName("gameType") val gameType : String,
	@SerializedName("gameVersion") val gameVersion : String,
	@SerializedName("mapId") val mapId : Int,
	@SerializedName("participants") val participants : List<Participants>,
	@SerializedName("platformId") val platformId : String,
	@SerializedName("queueId") val queueId : Int,
	@SerializedName("teams") val teams : List<Teams>,
	@SerializedName("tournamentCode") val tournamentCode : String
)