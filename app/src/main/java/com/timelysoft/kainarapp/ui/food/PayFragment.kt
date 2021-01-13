package com.timelysoft.kainarapp.ui.food


import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.loadingShow
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_pay.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLDecoder


class PayFragment : Fragment() {
    private val viewModel: FoodViewModel by viewModel()
    private var orderId = ""
    private val bundle = Bundle()
    private val successRegex = Regex("payment.*success")
    private val failRegex = Regex("payment.*fail")
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
        val html = try {
            requireArguments().getString("html")
        } catch (e: Exception) {
            ""
        }
        web_view.settings.javaScriptEnabled = true
        web_view.loadData(html, "text/html; charset=utf-8", "UTF-8")
        loadingHide()

        web_view.webViewClient = object : WebViewClient() {

            //            //todo
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
////                web_view.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();"
////                ) {
////                    println(it)
////                    println(url)
////                }
//            }
//
//
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                val decodedUrl = URLDecoder.decode(url, "UTF-8")
                val uri = Uri.parse(decodedUrl)
                println("find $decodedUrl")
                if (successRegex.containsMatchIn(decodedUrl)) {
                    println("success")
                    orderId = uri.getQueryParameter("orderId").toString()
                    if (orderId.isEmpty()) {
                        toast("OrderId is empty")
                    } else {
                        bundle.putString("orderId", orderId)
                        findNavController().navigate(R.id.nav_success_payment, bundle)
                    }
                } else if (failRegex.containsMatchIn(decodedUrl)) {
                    val text = uri.getQueryParameter("token").toString()
                    val message = URLDecoder.decode(text, "UTF-8")
                    bundle.putString("failMessage", message)
                    findNavController().navigate(R.id.nav_success_payment ,bundle)
                    /*
                    with(AlertDialog.Builder(requireContext()))
                    {
                        setTitle("Информация")
                        setMessage(message)
                        setPositiveButton("OK") { _, _ ->
                            findNavController().navigate(R.id.nav_home, bundle)
                        }
                        show()
                    }

                     */
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

        }
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_pay)
    }


}
