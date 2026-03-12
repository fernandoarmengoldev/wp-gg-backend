package com.fernandoarmengol.ggwp.model.match_nodejs

import com.google.gson.annotations.SerializedName

data class Perks (

	@SerializedName("statPerks") val statPerks : StatPerks,
	@SerializedName("styles") val styles : List<Styles>
)