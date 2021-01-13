package com.timelysoft.kainarapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.history.HistoryOrderListener
import com.timelysoft.kainarapp.adapter.history.HistoryOrdersAdapter
import com.timelysoft.kainarapp.adapter.restaurant.RestaurantListener
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseBottomSheet
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseListener
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.Status
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model.OrderItemModel
import com.timelysoft.kainarapp.service.model2.Restaurant
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.response.RestaurantInfoResponse
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_my_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), HistoryOrderListener,
    RestaurantChooseListener {
    private val viewModel: HistoryViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (!AppPreferences.isLogined) {
            val bundle = Bundle()
            bundle.putBoolean("back", true)
            val navBuilder = NavOptions.Builder()
            val navOptions = navBuilder.setPopUpTo(R.id.nav_home, false).build()
            findNavController().navigate(R.id.nav_auth, bundle, navOptions)
        }


        if (AppPreferences.lastOrderId.isEmpty()) {
            history_my_order_check.visibility = View.GONE
        }
        /*
        history_my_order_check.setOnClickListener {
            loadingShow()
            viewModel.orderStatuses().observe(viewLifecycleOwner, Observer {
                val orderStatuses = it.data
                when (it.status) {
                    Status.SUCCESS -> {
                        //error
                        if (AppPreferences.lastOrderType == 2) {
                            viewModel.checkLastOrder(
                                AppPreferences.lastOrderRestaurantId,
                                AppPreferences.lastOrderId
                            ).observe(viewLifecycleOwner, Observer { order ->
                                loadingHide()
                                when (order.status) {
                                    Status.SUCCESS -> {
                                        if (order?.data != null) {
                                            order.data.data.statusValue =
                                                orderStatuses?.first { it.id == order.data?.data?.status }!!.description
                                            val bundle = Bundle()
                                            bundle.putParcelable("result", order.data!!.data)
                                            findNavController().navigate(
                                                R.id.nav_order_detail,
                                                bundle
                                            )
                                        } else {
                                            toast("Ошибка при получении данных")
                                        }


                                    }
                                    else -> {
                                        toast(order.msg)
                                    }
                                }


                            })
                        } else if (AppPreferences.lastOrderType == 1) {
                            viewModel.checkLastOrderDemir(AppPreferences.lastOrderId)
                                .observe(viewLifecycleOwner, Observer { order ->
                                    loadingHide()
                                    when (order.status) {
                                        Status.SUCCESS -> {

                                            if (order?.data == null) {
                                                order.data?.data?.statusValue =
                                                    orderStatuses?.first { it.id == order.data?.data?.status }!!.description
                                                val bundle = Bundle()
                                                bundle.putParcelable("result", order.data!!.data)
                                                findNavController().navigate(
                                                    R.id.nav_order_detail,
                                                    bundle
                                                )
                                            } else {
                                                toast("Ошибка при получении данных")
                                            }
//                                            order.data?.data?.statusValue =
//                                                orderStatuses?.first { it.id == order.data?.data?.status }!!.description
//                                            val bundle = Bundle()
//                                            bundle.putParcelable("result", order.data!!.data)
//                                            findNavController().navigate(
//                                                R.id.nav_order_detail,
//                                                bundle
//                                            )
                                        }
                                        else -> {
                                            toast(order.msg)
                                        }
                                    }

                                })
                        }
                    }
                    else -> {
                        toast(it.msg)
                    }
                }


            })

        }

         */

        history_restaurants.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        loadingShow()
        viewModel.restaurants().observe(viewLifecycleOwner, Observer {response->
            loadingHide()
            response.doIfSuccess {
                val bottomSheetDialogFragment = RestaurantChooseBottomSheet(
                    this,
                    it as ArrayList<RestaurantResponse>
                )
                bottomSheetDialogFragment.isCancelable = AppPreferences.restaurant != ""
                bottomSheetDialogFragment.show(
                    childFragmentManager,
                    bottomSheetDialogFragment.tag
                )
            }
            response.doIfError {errorBody->
                errorBody?.getErrors {message->
                    toast(message)
                }

            }

        })

    }

    override fun onStart() {
        super.onStart()
        AppPreferences.change = true
    }

    override fun onStop() {
        super.onStop()
        AppPreferences.change = false
    }


    private fun getHistoryOrders(id: Int) {
        viewModel.orders(1, 50, id).observe(viewLifecycleOwner, Observer {
            loadingHide()
            it.doIfSuccess {historyOrder->
                history_orders_rv.adapter =
                    HistoryOrdersAdapter(this, historyOrder.items as ArrayList<OrderItemModel>)
            }

            it.doIfError {errorBody->
                errorBody?.getCrmErrors {msg->
                    toast(msg)
                }
            }
        })
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_history)
    }

    override fun onClickHistoryOrder(orderId: Int) {
        val bundle = Bundle()
        bundle.putInt("id", orderId)
        findNavController().navigate(R.id.nav_history_detail, bundle)
    }

    override fun onClickRestaurant(restaurantId: String, previousRestaurantId: String, crmId: Int) {
        getHistoryOrders(crmId)
    }
}
