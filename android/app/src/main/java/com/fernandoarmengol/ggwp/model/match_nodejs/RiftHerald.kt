package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class RiftHerald (

	@SerializedName("first") val first : Boolean,
	@SerializedName("kills") val kills : Int
)