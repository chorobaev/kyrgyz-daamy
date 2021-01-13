package com.timelysoft.kainarapp.ui.discount

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.image.ImagePageAdapter
import com.timelysoft.kainarapp.adapter.restaurant.ImageViewPagerAdapter
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.NewsResponse
import com.timelysoft.kainarapp.service.response.NewsResponseNew
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_news_detail.*


class NewsDetailFragment : Fragment() {
    private var newsModel: NewsResponse? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initToolbar()
        initData()
    }

    private fun initArguments() {
        newsModel = try {
            arguments?.getParcelable("newsDetail")
        } catch (e: Exception) {
            null
        }
    }

    private fun initData() {
        if (newsModel!!.boldDescription.isNotEmpty()) {
            discount_detail_bold_card.visibility = View.VISIBLE
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            discount_detail_description.text =
            Html.fromHtml(newsModel?.description, Html.FROM_HTML_MODE_COMPACT).trim()
            discount_detail_body.text = Html.fromHtml(newsModel?.boldDescription, Html.FROM_HTML_MODE_LEGACY).trim()
        } else {
            discount_detail_description.text = Html.fromHtml(newsModel?.description)
            discount_detail_body.text = Html.fromHtml(newsModel?.boldDescription)
        }


        val images = newsModel?.files?.map {
            AppPreferences.baseUrl + it.relativeUrl
        }
        discount_detail_view_pager.adapter = ImagePageAdapter(images as ArrayList<String>)
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = newsModel?.name

    }
}
