package com.timelysoft.kainarapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.order.OrderDiscountAdapter
import com.timelysoft.kainarapp.adapter.order.OrderItemsAdapter
import com.timelysoft.kainarapp.bottomsheet.paytype.PayTypeBottomSheet
import com.timelysoft.kainarapp.bottomsheet.paytype.PayTypeListener
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.loadingShow
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.Status
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model2.response2.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrderDetailFragment : Fragment(), PayTypeListener {

    private val viewModel: FoodViewModel by viewModel()
    private var order: OrderValidateResponse? = null
    private var orderCreateModel: CreateOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBundle()

        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    private fun initBundle() {
        order = try {
            arguments?.getParcelable("result")
        } catch (e: Exception) {
            println("NUll")
            null
        }

        orderCreateModel = try {
            arguments?.getParcelable("order")
        } catch (e: Exception) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()


        loadingHide()
        initAdapters()

        if (orderCreateModel == null) {
            order_detail_status.visibility = View.VISIBLE
            order_detail_pay.text = "Готово"
            //order_detail_status_value.text = order!!.status.toString()

        }
        order_detail_pay.setOnClickListener {
            if (orderCreateModel == null) {
                findNavController().popBackStack()
            } else {
                val bottom =
                    PayTypeBottomSheet(
                        this
                    )
                bottom.show(parentFragmentManager, bottom.tag)
            }

        }
        order_detail_sum.text = order!!.amount + " сом"


    }

    private fun initAdapters() {

        val orderItemsAdapter =
            OrderItemsAdapter(order!!.products as ArrayList<ResponseProductOrderState>)
        order_detail_items_rv.adapter = orderItemsAdapter

        val orderDiscountAdapter =
            OrderDiscountAdapter(order!!.discounts as ArrayList<Discount>)
        order_detail_discount_rv.adapter = orderDiscountAdapter
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_order_detail)
    }

    override fun onClickPay(payType: Int) {
        orderCreateModel!!.order.paymentType = payType
        if (payType == 3) {
            loadingShow()
            viewModel.orderCreate(orderCreateModel!!).observe(this, Observer { result->

                loadingHide()

                result.doIfSuccess {
                    it?.let{
                        AppPreferences.lastOrderType = payType
                        AppPreferences.lastOrderRestaurantId = AppPreferences.restaurant
                        AppPreferences.lastOrderId = it
                        payOnline(AppPreferences.restaurant, it)
                    }
                }

                result.doIfError {
                    it?.getErrors {msg->
                        toast(msg)
                    }
                }

            })
        } else {
            loadingShow()
            viewModel.orderCreate(orderCreateModel!!).observe(this, Observer { result ->
                loadingHide()
                result.doIfSuccess {
                    it?.let {lastOrderId->
                        AppPreferences.lastOrderType = payType
                        AppPreferences.lastOrderId = lastOrderId
                        AppPreferences.lastOrderRestaurantId = AppPreferences.restaurant
                        accessDialog()
                    }
                }

                result.doIfError {
                    it?.getErrors {msg->
                        toast(msg)
                    }
                }
            })
        }

    }

    private fun payOnline(restaurantId: String, orderId: String) {
        viewModel.payOnline(restaurantId, orderId).observe(this, Observer { response ->

            response.doIfSuccess { onlinePaymentResponse ->
                if (onlinePaymentResponse != null) {
                    val html = generateHtml(onlinePaymentResponse)
                    val bundle = Bundle()
                    bundle.putString("html", html)
                    findNavController().navigate(R.id.nav_pay, bundle)
                }
            }
            response.doIfError {
                it?.getErrors {msg->
                    toast(msg)
                }
            }
        })
    }

    private fun accessDialog() {
        with(AlertDialog.Builder(requireContext()))
        {
            setTitle("Информация")
            setMessage("Спасибо за покупку")
            setPositiveButton("OK") { _, _ ->
                //viewModel.clearBasket()
                findNavController().navigate(R.id.nav_home)
            }
            show()
        }
    }


    private fun generateHtml(order: OnlinePaymentResponse): String {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body> " +
                "<form  id=\"myForm\" method=\"post\" action=\"https://entegrasyon.asseco-see.com.tr/fim/est3Dgate\"> " +
                "<input type=\"hidden\" name=\"clientid\" value=\"${order.clientId}\"/> " +
                "<input type=\"hidden\" name=\"storetype\" value=\"${order.storeType}\" /> " +
                "<input type=\"hidden\" name=\"hash\" value=\"${order.hash}\" /> " +
                "<input type=\"hidden\" name=\"trantype\" value=\"Auth\" /> " +
                "<input type=\"hidden\" name=\"amount\" value=\"${order.amount}\" /> " +
                "<input type=\"hidden\" name=\"currency\" value=\"${order.currency}\" /> " +
                "<input type=\"hidden\" name=\"oid\" value=\"${order.paymentId}\" /> " +
                "<input type=\"hidden\" name=\"okUrl\" value=\"${order.okUrl}\"/> " +
                "<input type=\"hidden\" name=\"failUrl\" value=\"${order.failUrl}\" /> " +
                "<input type=\"hidden\" name=\"lang\" value=\"${order.language}\" /> " +
                "<input type=\"hidden\" name=\"rnd\" value=\"${order.rnd}\" /> " +
                "<input type=\"hidden\" name=\"encoding\" value=\"utf-8\" /> " +
                "<input type=\"hidden\" name=\"refreshtime\" value=\"${order.refreshTime}\" /> " +
                "<input type=\"hidden\" value=\"Оплатить\">" +
                "</form>" +
                "\n" +
                "<script>\n" +
                "function myFunction() {\n" +
                "  document.getElementById(\"myForm\").submit();\n" +
                "}\n" +
                "myFunction()\n" +
                "</script>" +
                "</body>\n" +
                "</html>"
    }


}
