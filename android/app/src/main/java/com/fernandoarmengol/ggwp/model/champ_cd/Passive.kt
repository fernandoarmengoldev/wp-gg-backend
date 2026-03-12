package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class Passive (

	@SerializedName("name") val name : String,
	@SerializedName("abilityIconPath") val abilityIconPath : String,
	@SerializedName("abilityVideoPath") val abilityVideoPath : String,
	@SerializedName("abilityVideoImagePath") val abilityVideoImagePath : String,
	@SerializedName("description") val description : String
)