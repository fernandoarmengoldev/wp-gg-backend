package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class ChampCD (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("alias") val alias : String,
	@SerializedName("title") val title : String,
	@SerializedName("shortBio") val shortBio : String,
	@SerializedName("tacticalInfo") val tacticalInfo : TacticalInfo,
	@SerializedName("playstyleInfo") val playstyleInfo : PlaystyleInfo,
	@SerializedName("squarePortraitPath") val squarePortraitPath : String,
	@SerializedName("stingerSfxPath") val stingerSfxPath : String,
	@SerializedName("chooseVoPath") val chooseVoPath : String,
	@SerializedName("banVoPath") val banVoPath : String,
	@SerializedName("roles") val roles : List<String>,
	@SerializedName("recommendedItemDefaults") val recommendedItemDefaults : List<String>,
	@SerializedName("skins") val skins : List<Skins>,
	@SerializedName("passive") val passive : Passive,
	@SerializedName("spells") val spells : List<Spells>
)