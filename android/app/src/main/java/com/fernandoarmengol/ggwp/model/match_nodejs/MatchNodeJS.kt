package com.fernandoarmengol.ggwp.model.match_nodejs

import com.fernandoarmengol.ggwp.model.match_nodejs.Info
import com.fernandoarmengol.ggwp.model.match_nodejs.Metadata
import com.google.gson.annotations.SerializedName

data class MatchNodeJS (

	@SerializedName("metadata") val metadata : Metadata,
	@SerializedName("info") val info : Info
)