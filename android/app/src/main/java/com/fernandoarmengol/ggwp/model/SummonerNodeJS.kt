package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class SummonerNodeJS(
    @SerializedName("id") val id : String,
    @SerializedName("accountId") val accountId : String,
    @SerializedName("puuid") val puuid : String,
    @SerializedName("name") val name : String,
    @SerializedName("profileIconId") val profileIconId : Int,
    @SerializedName("revisionDate") val revisionDate : Long,
    @SerializedName("summonerLevel") val summonerLevel : Int,
    @SerializedName("history") val history : List<String>
)