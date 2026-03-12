package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Styles (

	@SerializedName("description") val description : String,
	@SerializedName("selections") val selections : List<Selections>,
	@SerializedName("style") val style : Int
)