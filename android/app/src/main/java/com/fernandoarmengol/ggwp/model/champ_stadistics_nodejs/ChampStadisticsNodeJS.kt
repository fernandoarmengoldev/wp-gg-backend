package com.fernandoarmengol.ggwp.model.champ_stadistics_nodejs

import com.google.gson.annotations.SerializedName

data class ChampStadisticsNodeJS (

	@SerializedName("winRate") val winRate : Double,
	@SerializedName("pickRate") val pickRate : Double,
	@SerializedName("bestFive") val bestFive : List<BestFive>,
	@SerializedName("worstFive") val worstFive : List<WorstFive>,
	@SerializedName("boots") val boots : List<Boots>,
	@SerializedName("mythics") val mythics : List<Mythics>,
	@SerializedName("otherItem") val otherItem : List<OtherItem>,
	@SerializedName("spells") val spells : List<Spells>,
	@SerializedName("runes") val runes : List<Runes>
)