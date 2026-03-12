package com.fernandoarmengol.ggwp.model.champ_cd

import com.google.gson.annotations.SerializedName

data class EffectAmounts (

	@SerializedName("Effect1Amount") val effect1Amount : List<Double>,
	@SerializedName("Effect2Amount") val effect2Amount : List<Double>,
	@SerializedName("Effect3Amount") val effect3Amount : List<Double>,
	@SerializedName("Effect4Amount") val effect4Amount : List<Double>,
	@SerializedName("Effect5Amount") val effect5Amount : List<Double>,
	@SerializedName("Effect6Amount") val effect6Amount : List<Double>,
	@SerializedName("Effect7Amount") val effect7Amount : List<Double>,
	@SerializedName("Effect8Amount") val effect8Amount : List<Double>,
	@SerializedName("Effect9Amount") val effect9Amount : List<Double>,
	@SerializedName("Effect10Amount") val effect10Amount : List<Double>
)