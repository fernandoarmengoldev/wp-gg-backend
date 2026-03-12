package com.fernandoarmengol.ggwp.model

import com.google.gson.annotations.SerializedName

data class ItemsCD (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("active") val active : Boolean,
    @SerializedName("inStore") val inStore : Boolean,
    @SerializedName("from") val from : List<String>,
    @SerializedName("to") val to : List<Int>,
    @SerializedName("categories") val categories : List<String>,
    @SerializedName("maxStacks") val maxStacks : Int,
    @SerializedName("requiredChampion") val requiredChampion : String,
    @SerializedName("requiredAlly") val requiredAlly : String,
    @SerializedName("requiredBuffCurrencyName") val requiredBuffCurrencyName : String,
    @SerializedName("requiredBuffCurrencyCost") val requiredBuffCurrencyCost : Int,
    @SerializedName("specialRecipe") val specialRecipe : Int,
    @SerializedName("isEnchantment") val isEnchantment : Boolean,
    @SerializedName("price") val price : Int,
    @SerializedName("priceTotal") val priceTotal : Int,
    @SerializedName("iconPath") val iconPath : String
)