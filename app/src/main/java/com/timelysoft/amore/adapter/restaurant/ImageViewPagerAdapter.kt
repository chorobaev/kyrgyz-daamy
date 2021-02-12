package com.timelysoft.amore.adapter.restaurant

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.timelysoft.amore.R

class ImageViewPagerAdapter(val context : Context, val list: List<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return if (list.isEmpty()){
            1
        } else {
            list.size
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)

        when{
            list.isEmpty()->{
                imageView.setImageResource(R.drawable.img_placeholder_not_found)
            }
            else->{
                Glide.with(context)
                    .load(list[position])
                    .optionalCenterCrop()
                    .thumbnail(Glide.with(context).load(R.drawable.img_placeholder_not_found))
                    .into(imageView)
            }
        }
        container.addView(imageView)

        return imageView
    }


}