package com.timelysoft.kainarapp.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timelysoft.kainarapp.R


fun ImageView.loadImage(url: String?) {
    try {
        Glide.with(context)
            .load(url)
            .error(R.drawable.img_placeholder_loading)
            .thumbnail(Glide.with(this).load(R.drawable.img_placeholder_not_found))
            .into(this)
    } catch (e: Exception) {
          
    }
}

fun ImageView.loadImageOrHide(url: String?) {
    try {
        Glide.with(context)
            .load(url)
//            .error(R.drawable.img_placeholder_loading)
//            .thumbnail(Glide.with(this).load(R.drawable.img_placeholder_not_found))
            .transform(RoundedCorners(12))
            .into(this)
    } catch (e: Exception) {
        println()
    }

}

fun ImageView.loadImageWithoutCorner(url: String?) {
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