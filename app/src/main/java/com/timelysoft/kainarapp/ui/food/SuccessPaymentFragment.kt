package com.timelysoft.kainarapp.ui.food

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.success_payment_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuccessPaymentFragment : Fragment() {
    private var orderId: String? = null
    private var failMessage : String? = null
    private val STORAGE_PERMISION_CODE: Int = 1000
    private val viewModel: FoodViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null){
            orderId = requireArguments().getString("orderId", "")
            failMessage = requireArguments().getString("failMessage", "")
        }
        return inflater.inflate(R.layout.success_payment_fragment,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!failMessage.isNullOrEmpty()){
            downloadCheckPayment.visibility = View.GONE
            successPayment.visibility = View.GONE
            success_payment_message.visibility = View.GONE
            failPaymentMessage.visibility = View.VISIBLE

            viewModel.getExceptionOfPayment(failMessage!!).observe(viewLifecycleOwner, Observer { resource->
                var message = ""
                resource.doIfSuccess {
                    if (it.containsKey("RawText")){
                        it["RawText"]?.forEach {text->
                            println(text)
                            message += "$text "
                        }
                    }
                    failPaymentMessage.text = message
                }
                resource.doIfError {
                    it?.getErrors { msg->
                        toast(msg)
                    }
                }
            })
        }

        toolbar_back.setOnClickListener {
            findNavController().popBackStack(R.id.nav_home, true)

        }
        downloadCheckPayment.setOnClickListener {
            if (orderId != null){
                downloadCheck(orderId!!)
            }
        }
    }
    private fun downloadCheck(orderId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
//                permission denied
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permissions, STORAGE_PERMISION_CODE)


            } else {
                downloadFile(orderId)

            }
        } else {

            downloadFile(orderId)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        loadingHide()
        when (requestCode) {
            STORAGE_PERMISION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    findNavController().navigate(R.id.nav_home)
                } else {
                    downloadFile(orderId!!)
                }
            }
        }
    }

    private fun downloadFile(orderId: String) {
       //loadingShow()
        val downloadUrl = "${AppPreferences.baseUrl}api/orders/${orderId}/checks/pdf"
        Toast.makeText(context, "Файл загружается...", Toast.LENGTH_LONG).show()
        val reguest = DownloadManager.Request(Uri.parse(downloadUrl))
        reguest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        reguest.setTitle("Kaynar")
        reguest.setDescription("Файл загружается...")

        reguest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        reguest.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${System.currentTimeMillis()}.pdf"
        )

        val manager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(reguest)
        findNavController().navigate(R.id.nav_home)


    }

}