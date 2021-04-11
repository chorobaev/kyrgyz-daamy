package io.flaterlab.kyrgyzdaamy.extension

import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import io.flaterlab.kyrgyzdaamy.R


fun ImageView.loadImageCoil(url: String) {
    this.load(url) {
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