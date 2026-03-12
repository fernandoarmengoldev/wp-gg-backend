package com.fernandoarmengol.ggwp.network

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

object GlideHelper {
    private val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
        .setBaseAlpha(0.7f) //the alpha of the underlying children
        .setHighlightAlpha(0.6f) // the shimmer alpha amount
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()
    // This is the placeholder for the imageView
    private val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
    fun loadImage(url: String, imgView: ImageView){
        Glide.with(imgView.context)
            .load(url)
            .placeholder(shimmerDrawable)
            .into(imgView)
    }
}