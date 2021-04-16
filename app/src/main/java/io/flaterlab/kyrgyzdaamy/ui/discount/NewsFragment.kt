package io.flaterlab.kyrgyzdaamy.ui.discount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.news.NewsAdapter
import io.flaterlab.kyrgyzdaamy.adapter.news.NewsListener
import io.flaterlab.kyrgyzdaamy.databinding.FragmentNewsBinding
import io.flaterlab.kyrgyzdaamy.extension.*
import io.flaterlab.kyrgyzdaamy.service.*
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {
    @ExperimentalCoroutinesApi
    private val viewModel: NewsViewModel by viewModels()

    private val binding by viewBinding(FragmentNewsBinding::bind)


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()) {
                binding.discountRv.visibility = View.VISIBLE
                binding.noInternetLayout.root.visibility = View.GONE

            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.newsStateFlow.collect {
                val newsAdapter = NewsAdapter(it as ArrayList<NewsResponse>)
                binding.discountRv.adapter = newsAdapter
            }
        }

    }





    private fun initToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_discount)
    }



}
