package com.fernandoarmengol.ggwp.model.perk_styles_cd

import com.google.gson.annotations.SerializedName

data class Styles (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("tooltip") val tooltip : String,
	@SerializedName("iconPath") val iconPath : String,
	@SerializedName("assetMap") val assetMap : AssetMap,
	@SerializedName("isAdvanced") val isAdvanced : Boolean,
	@SerializedName("allowedSubStyles") val allowedSubStyles : List<Int>,
	@SerializedName("subStyleBonus") val subStyleBonus : List<SubStyleBonus>,
	@SerializedName("slots") val slots : List<Slots>,
	@SerializedName("defaultPageName") val defaultPageName : String,
	@SerializedName("defaultSubStyle") val defaultSubStyle : Int,
	@SerializedName("defaultPerks") val defaultPerks : List<Int>,
	@SerializedName("defaultPerksWhenSplashed") val defaultPerksWhenSplashed : List<Int>,
	@SerializedName("defaultStatModsPerSubStyle") val defaultStatModsPerSubStyle : List<DefaultStatModsPerSubStyle>
)