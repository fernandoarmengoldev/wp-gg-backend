package com.fernandoarmengol.ggwp.model.perk_styles_cd

import com.google.gson.annotations.SerializedName

data class PerkStyleCD (

	@SerializedName("schemaVersion") val schemaVersion : Int,
	@SerializedName("styles") val styles : List<Styles>
)