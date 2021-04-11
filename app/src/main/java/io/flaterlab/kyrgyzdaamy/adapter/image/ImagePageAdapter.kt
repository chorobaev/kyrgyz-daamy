package io.flaterlab.kyrgyzdaamy.adapter.image

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import io.flaterlab.kyrgyzdaamy.extension.loadImageGlide

class ImagePageAdapter(private val images: ArrayList<String> = ArrayList()) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image = ImageView(container.context)
        image.loadImageGlide(images[position])
        image.adjustViewBounds = true
        image.scaleType = ImageView.ScaleType.FIT_XY
        container.addView(image)
        return image
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}