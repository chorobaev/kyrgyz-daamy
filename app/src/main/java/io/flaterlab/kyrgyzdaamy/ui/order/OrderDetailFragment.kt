package io.flaterlab.kyrgyzdaamy.ui.order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.order.OrderDiscountAdapter
import io.flaterlab.kyrgyzdaamy.adapter.order.OrderItemsAdapter
import io.flaterlab.kyrgyzdaamy.bottomsheet.paytype.PayTypeBottomSheet
import io.flaterlab.kyrgyzdaamy.bottomsheet.paytype.PayTypeListener
import io.flaterlab.kyrgyzdaamy.databinding.FragmentOrderDetailBinding
import io.flaterlab.kyrgyzdaamy.extension.getErrors
import io.flaterlab.kyrgyzdaamy.extension.loadingHide
import io.flaterlab.kyrgyzdaamy.extension.loadingShow
import io.flaterlab.kyrgyzdaamy.extension.toast
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.doIfError
import io.flaterlab.kyrgyzdaamy.service.doIfSuccess
import io.flaterlab.kyrgyzdaamy.service.response.*
import io.flaterlab.kyrgyzdaamy.ui.food.FoodViewModel
import io.flaterlab.kyrgyzdaamy.ui.viewBinding

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_order_detail), PayTypeListener {

    private val viewModel: FoodViewModel by viewModels()
    private var order: OrderValidateResponse? = null
    private var orderCreateModel: CreateOrder? = null

    private val binding by viewBinding(FragmentOrderDetailBinding::bind)


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
            binding.orderDetailStatus.visibility = View.VISIBLE
            binding.orderDetailPay.text = "Готово"

        }
        binding.orderDetailPay.setOnClickListener {
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
        binding.orderDetailSum.text = order!!.amount + " ${AppPreferences.currencyName}"


    }

    private fun initAdapters() {

        val orderItemsAdapter =
            OrderItemsAdapter(order!!.products as ArrayList<ResponseProductOrderState>)
        binding.orderDetailItemsRv.adapter = orderItemsAdapter

        val orderDiscountAdapter =
            OrderDiscountAdapter(order!!.discounts as ArrayList<Discount>)
        binding.orderDetailDiscountRv.adapter = orderDiscountAdapter
    }

    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            binding.toolbar.toolbarBack.visibility = View.VISIBLE
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_order_detail)
        binding.toolbar.toolbarBack.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }

    override fun onClickPay(payType: Int) {
        orderCreateModel!!.order.paymentType = payType
        when (payType) {
            3 -> {
                loadingShow()
                viewModel.orderCreate(orderCreateModel!!).observe(this, Observer { result ->

                    loadingHide()

                    result.doIfSuccess {

                        AppPreferences.lastOrderType = payType
                        AppPreferences.lastOrderRestaurantId = AppPreferences.restaurant
                        payOnline(AppPreferences.restaurant, it.data)

                    }
                    result.doIfError {
                        it?.getErrors { msg ->
                            toast(msg)
                        }
                    }

                })
            }
            2 -> {
                doNothing(2)
            }
            1 -> {
                doNothing(1)
            }


        }

    }

    private fun doNothing(payType: Int) {
        loadingShow()
        viewModel.orderCreate(orderCreateModel!!).observe(this, Observer { result ->
            loadingHide()
            result.doIfSuccess {

                AppPreferences.lastOrderType = payType
                AppPreferences.lastOrderRestaurantId = AppPreferences.restaurant
                accessDialog()

            }

            result.doIfError {
                it?.getErrors { msg ->
                    toast(msg)
                }
            }
        })
    }

    private fun payOnline(restaurantId: String, orderId: String) {
        viewModel.payOnline(restaurantId, orderId).observe(this, Observer { response ->

            response.doIfSuccess { url ->
                if (url != null) {
                    val bundle = Bundle()
                    bundle.putString("url", url.data.robokassaPayRedirectionUrl)
                    bundle.putString("html", generateHtml(url.data.robokassaCredentialsData))
                    findNavController().navigate(R.id.nav_pay, bundle)
                }
            }
            response.doIfError {
                it?.getErrors { msg ->
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
                BasketCommands.deleteAllFromBasket()
                findNavController().popBackStack(R.id.nav_basket, true)
            }
            show()
        }
    }

    private fun generateHtml(credentialsData: RobokassaCredentialsData): String {
        return "<!DOCTYPE html>" +
                "<html>\n" +
                "<form id=\"myForm\"action='https://auth.robokassa.ru/Merchant/Index.aspx' method=POST>" +
                "<input type=hidden name=MerchantLogin value=${credentialsData.MerchantLogin}>" +
                "<input type=hidden name=OutSum value=${credentialsData.OutSum}>" +
                "<input type=hidden name=InvId value=${credentialsData.InvoiceID}>" +
                "<input type=hidden name=Description value='${credentialsData.Description}'>" +
                "<input type=hidden name=SignatureValue value=${credentialsData.SignatureValue}>" +
                "<input type=hidden name=IsTest value=${credentialsData.IsTest}>" +
                "<input type=hidden value='Оплатить'>" +
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
