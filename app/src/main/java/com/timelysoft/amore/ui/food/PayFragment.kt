package com.timelysoft.amore.ui.food


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_pay.*
import java.net.URLDecoder


class PayFragment : Fragment() {
    private var orderId = ""
    private val bundle = Bundle()
    private val successRegex = Regex("payment.*success")
    private val failRegex = Regex("payment.*failed")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bundle.putBoolean("loading", true)
        return inflater.inflate(R.layout.fragment_pay, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        loadingShow()

        //val credentials = requireArguments().getParcelable<RobokassaCredentialsData>("robokassa")
        //val credentialsData = Gson().toJson(credentials)
        //val url = requireArguments().getString("url")
        web_view.settings.javaScriptEnabled = true
        val html = requireArguments().getString("html")

       // web_view.postUrl(url, credentialsData.toUtf8Bytes())
        //web_view.loadUrl(url)
       web_view.loadData(html, "text/html; charset=utf-8", "UTF-8")
        loadingHide()

        web_view.webViewClient = object : WebViewClient() {


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
            toolbar_back.visibility = View.VISIBLE
        }
        toolbar_text.text = getString(R.string.menu_pay)
        toolbar_back.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }



}
