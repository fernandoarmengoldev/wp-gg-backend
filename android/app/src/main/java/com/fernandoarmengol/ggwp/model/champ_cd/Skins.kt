package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class Skins (

	@SerializedName("id") val id : Int,
	@SerializedName("isBase") val isBase : Boolean,
	@SerializedName("name") val name : String,
	@SerializedName("splashPath") val splashPath : String,
	@SerializedName("uncenteredSplashPath") val uncenteredSplashPath : String,
	@SerializedName("tilePath") val tilePath : String,
	@SerializedName("loadScreenPath") val loadScreenPath : String,
	@SerializedName("skinType") val skinType : String,
	@SerializedName("rarity") val rarity : String,
	@SerializedName("isLegacy") val isLegacy : Boolean,
	@SerializedName("splashVideoPath") val splashVideoPath : String,
	@SerializedName("collectionSplashVideoPath") val collectionSplashVideoPath : String,
	@SerializedName("featuresText") val featuresText : String,
	@SerializedName("chromaPath") val chromaPath : String,
	@SerializedName("emblems") val emblems : String,
	@SerializedName("regionRarityId") val regionRarityId : Int,
	@SerializedName("rarityGemPath") val rarityGemPath : String,
	@SerializedName("skinLines") @Transient val skinLines : String,
	@SerializedName("description") val description : String
)