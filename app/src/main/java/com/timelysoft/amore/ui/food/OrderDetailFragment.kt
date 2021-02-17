package com.timelysoft.amore.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.BasketCommands
import com.timelysoft.amore.adapter.order.OrderDiscountAdapter
import com.timelysoft.amore.adapter.order.OrderItemsAdapter
import com.timelysoft.amore.bottomsheet.paytype.PayTypeBottomSheet
import com.timelysoft.amore.bottomsheet.paytype.PayTypeListener
import com.timelysoft.amore.extension.getErrors
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.doIfError
import com.timelysoft.amore.service.doIfSuccess
import com.timelysoft.amore.service.response.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class OrderDetailFragment : Fragment(), PayTypeListener {

    private val viewModel: FoodViewModel by sharedViewModel()
    private var order: OrderValidateResponse? = null
    private var orderCreateModel: CreateOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        initBundle()
        initToolbar()

        loadingHide()
        initAdapters()

        if (orderCreateModel == null) {
            order_detail_status.visibility = View.VISIBLE
            order_detail_pay.text = "Готово"

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
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            toolbar_back.visibility = View.VISIBLE
        }
        toolbar_text.text = getString(R.string.menu_order_detail)
        toolbar_back.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }

    override fun onClickPay(payType: Int) {
        orderCreateModel!!.order.paymentType = payType
        when(payType){
            3 ->{
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
            }
            2->{
               doNothing(2)
            }
            1->{
                doNothing(1)
            }


        }

    }
    private fun doNothing(payType: Int){
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

    private fun payOnline(restaurantId: String, orderId: String) {
        viewModel.payOnline(restaurantId, orderId).observe(this, Observer {response->

            response.doIfSuccess { url->
                if (url != null) {
                    val bundle = Bundle()
                    bundle.putString("url",url.robokassaPayRedirectionUrl)
                    bundle.putString("html",generateHtml(url.robokassaCredentialsData))
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
                BasketCommands.deleteAll()
                findNavController().popBackStack(R.id.nav_basket, true)
            }
            show()
        }
    }
    private fun generateHtml(credentialsData: RobokassaCredentialsData) : String{
        return "<!DOCTYPE html>"+
                "<html>\n"+
                "<form id=\"myForm\"action='https://auth.robokassa.ru/Merchant/Index.aspx' method=POST>"+
                "<input type=hidden name=MerchantLogin value=${credentialsData.MerchantLogin}>"+
                "<input type=hidden name=OutSum value=${credentialsData.OutSum}>"+
                "<input type=hidden name=InvId value=${credentialsData.InvoiceID}>"+
                "<input type=hidden name=Description value='${credentialsData.Description}'>"+
                "<input type=hidden name=SignatureValue value=${credentialsData.SignatureValue}>"+
                "<input type=hidden name=IsTest value=${credentialsData.IsTest}>"+
                "<input type=hidden value='Оплатить'>"+
                "</form>" +
                "<script>" +
                "function myFunction() {" +
                "  document.getElementById(\"myForm\").submit();" +
                "}" +
                "myFunction()" +
                "</script>" +
                "</html>"


    }


}
