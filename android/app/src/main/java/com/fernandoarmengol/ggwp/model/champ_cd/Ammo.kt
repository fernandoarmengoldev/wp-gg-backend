package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class Ammo (

	@SerializedName("ammoRechargeTime") val ammoRechargeTime : List<Double>,
	@SerializedName("maxAmmo") val maxAmmo : List<Double>
)