package com.timelysoft.amore.ui.pay


import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.databinding.FragmentPayBinding
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import com.timelysoft.amore.ui.viewBinding
import java.net.URLDecoder


class PayFragment : Fragment(R.layout.fragment_pay) {
    private val bundle = Bundle()
    private val successRegex = Regex("payment.*success")
    private val failRegex = Regex("payment.*failed")
    private val binding by viewBinding(FragmentPayBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        loadingShow()


        binding.webView.settings.javaScriptEnabled = true
        val html = requireArguments().getString("html")


        binding.webView.loadData(html, "text/html; charset=utf-8", "UTF-8")
        loadingHide()

        binding.webView.webViewClient = object : WebViewClient() {


            override fun onLoadResource(view: WebView?, url: String?) {
                val decodedUrl = URLDecoder.decode(url, "UTF-8")
                val uri = Uri.parse(decodedUrl)
                println("find $decodedUrl")
                if (successRegex.containsMatchIn(decodedUrl)) {
                    println("success")
                    findNavController().navigate(R.id.toSuccessPaymentFragment)

                } else if (failRegex.containsMatchIn(decodedUrl)) {
                    val text = uri.getQueryParameter("token").toString()
                    val message = URLDecoder.decode(text, "UTF-8")
                    bundle.putString("failMessage", message)
                    findNavController().navigate(R.id.toSuccessPaymentFragment, bundle)
                }
                super.onLoadResource(view, url)
            }

        }
    }

    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            binding.toolbar.toolbarBack.visibility = View.VISIBLE
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_pay)
        binding.toolbar.toolbarBack.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }


}
