package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class PerksCD(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("majorChangePatchVersion") @Transient val majorChangePatchVersion : Double,
    @SerializedName("tooltip") val tooltip : String,
    @SerializedName("shortDesc") val shortDesc : String,
    @SerializedName("longDesc") val longDesc : String,
    @SerializedName("iconPath") val iconPath : String,
    @SerializedName("endOfGameStatDescs") @Transient val endOfGameStatDescs : List<String>
)