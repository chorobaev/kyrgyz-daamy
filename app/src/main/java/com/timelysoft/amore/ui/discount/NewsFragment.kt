package com.timelysoft.amore.ui.discount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.news.NewsAdapter
import com.timelysoft.amore.adapter.news.NewsListener
import com.timelysoft.amore.extension.getErrors
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.NewsResponse
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsFragment : Fragment(), NewsListener {
    private val viewModel: NewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initData()


    }

    private fun initData() {
        loadingShow()
                        viewModel.news(AppPreferences.restaurant)
                            .observe(viewLifecycleOwner, Observer { news ->
                                loadingHide()
                                news.doIfSuccess {
                                    val adapter =
                                        NewsAdapter(this, it?.data as ArrayList<NewsResponse>)
                                    discount_rv.adapter = adapter
                                }

                                news.doIfError { errorBody ->
                                    errorBody?.getErrors { msg ->
                                        toast(msg)
                                    }
                                }
                            })
                    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_discount)
    }

    override fun onDiscountCLick(item: NewsResponse) {
        val bundle = Bundle()
        bundle.putParcelable("newsDetail", item)
        findNavController().navigate(R.id.nav_discount_detail, bundle)
    }


}