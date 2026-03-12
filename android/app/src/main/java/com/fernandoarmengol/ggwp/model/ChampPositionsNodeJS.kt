package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class ChampPositionsNodeJS (

    @SerializedName("value") val value : Int,
    @SerializedName("positions") val positions : List<String>
)