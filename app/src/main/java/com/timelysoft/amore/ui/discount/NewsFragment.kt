package com.timelysoft.amore.ui.discount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.news.NewsAdapter
import com.timelysoft.amore.adapter.news.NewsListener
import com.timelysoft.amore.databinding.FragmentNewsBinding
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.NewsResponse
import com.timelysoft.amore.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsListener {
    private val viewModel: NewsViewModel by viewModels()

    private val binding by viewBinding(FragmentNewsBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initData()

        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()){
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
                news.doIfNetwork {errorType->
                    when(errorType){
                        is ErrorTypes.TimeOutError->{
                            toast(errorType.msg)
                        }
                        is ErrorTypes.ConnectionError->{
                            binding.discountRv.visibility = View.GONE
                            binding.noInternetLayout.root.visibility = View.VISIBLE
                        }
                        is ErrorTypes.EmptyResultError->{
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
