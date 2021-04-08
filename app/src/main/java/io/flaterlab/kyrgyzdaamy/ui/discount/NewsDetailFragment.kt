package io.flaterlab.kyrgyzdaamy.ui.discount

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.image.ImagePageAdapter
import io.flaterlab.kyrgyzdaamy.databinding.FragmentNewsDetailBinding
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse
import io.flaterlab.kyrgyzdaamy.ui.viewBinding


class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private var newsModel: NewsResponse? = null

    private val binding by viewBinding(FragmentNewsDetailBinding::bind)

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.discountDetailDescription.text =
                Html.fromHtml(newsModel?.description, Html.FROM_HTML_MODE_COMPACT).trim()
            binding.discountDetailBody.text =
                Html.fromHtml(newsModel?.boldDescription, Html.FROM_HTML_MODE_LEGACY).trim()
        } else {
            binding.discountDetailDescription.text = Html.fromHtml(newsModel?.description)
            binding.discountDetailBody.text = Html.fromHtml(newsModel?.boldDescription)
        }


        val images = newsModel?.files?.map {
            AppPreferences.baseUrl + it.relativeUrl
        }
        binding.discountDetailViewPager.adapter = ImagePageAdapter(images as ArrayList<String>)
    }

    private fun initToolbar() {
        binding.toolbar.toolbarBack.visibility = View.VISIBLE
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_discount)

    }
}
