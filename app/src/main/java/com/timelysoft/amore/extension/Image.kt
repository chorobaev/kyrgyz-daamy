package com.timelysoft.amore.extension

import android.widget.ImageView
import coil.load
import coil.loadAny
import coil.size.Scale
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide

import com.timelysoft.amore.R



fun ImageView.loadImageCoil(url:String){
    this.load(url){
        placeholder(R.drawable.img_placeholder_loading)
        error(R.drawable.img_placeholder_not_found)
        transformations(RoundedCornersTransformation())
    }
}

fun ImageView.loadImageGlide(url: String?) {
    try {
        Glide.with(context)
            .load(url)
            .error(R.drawable.img_placeholder_loading)
            .thumbnail(Glide.with(this).load(R.drawable.img_placeholder_not_found))
            .fitCenter()
            .into(this)
    } catch (e: Exception) {
    }
}