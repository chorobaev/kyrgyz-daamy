package io.flaterlab.kyrgyzdaamy.ui.discount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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


@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsListener {
    private val viewModel: NewsViewModel by viewModels()

    private val binding by viewBinding(FragmentNewsBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initData()

        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()) {
                binding.discountRv.visibility = View.VISIBLE
                binding.noInternetLayout.root.visibility = View.GONE
                initData()
            }
        }

    }


    private fun initData() {
        loadingShow()
        viewModel.news(AppPreferences.restaurant)
            .observe(viewLifecycleOwner, { news ->
                loadingHide()
                news.doIfSuccess {
                    val adapter =
                        NewsAdapter(this, it.data as ArrayList<NewsResponse>)
                    binding.discountRv.adapter = adapter
                }

                news.doIfError { errorBody ->
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }
                }
                news.doIfNetwork { errorType ->
                    when (errorType) {
                        is ErrorTypes.TimeOutError -> {
                            toast(errorType.msg)
                        }
                        is ErrorTypes.ConnectionError -> {
                            binding.discountRv.visibility = View.GONE
                            binding.noInternetLayout.root.visibility = View.VISIBLE
                        }
                        is ErrorTypes.EmptyResultError -> {
                            toast(errorType.msg)
                        }
                    }

                }
            })
    }

    private fun initToolbar() {
        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_discount)
    }

    override fun onDiscountCLick(item: NewsResponse) {
        val bundle = Bundle()
        bundle.putParcelable("newsDetail", item)
        findNavController().navigate(R.id.nav_discount_detail, bundle)
    }


}
