package com.timelysoft.kainarapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.history.HistoryOrdersDetailAdapter
import com.timelysoft.kainarapp.adapter.order.OrderDiscountAdapter
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.Status
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model.HistoryMenuItems
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import com.timelysoft.kainarapp.service.model2.response2.Discount
import com.timelysoft.kainarapp.service.response.OrderDiscountResponse
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_history_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryDetailFragment : Fragment() {
    private var historyId: Int = 0
    private val viewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_history_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initToolbar()
        initData()
    }

    private fun initArguments() {
        historyId = try {
            arguments?.getInt("id")!!
        } catch (e: Exception) {
            0
        }
    }

    private fun initData() {
        loadingShow()
        viewModel.orderDetail(historyId).observe(viewLifecycleOwner, Observer {
            loadingHide()

            it.doIfSuccess {historyOrderDetail->
                history_detail_name.text = "${historyOrderDetail!!.clientFirstName} ${historyOrderDetail.clientLastName}"

                when (historyOrderDetail.orderType) {
                    0 -> history_detail_type_order.text = "Тип заказа: Неизвестно"
                    1 -> history_detail_type_order.text = "Тип заказа: Доставка"
                    2 -> history_detail_type_order.text = "Тип заказа: Самовывоз"
                    3 -> history_detail_type_order.text = "Тип заказа: На месте"
                }

                when (historyOrderDetail.payType) {
                    0 -> history_detail_type_pay.text = "Тип оплаты: Неизвестно"
                    1 -> history_detail_type_pay.text = "Тип оплаты: Наличные"
                    2 -> history_detail_type_pay.text = "Тип оплаты: Банковская карта"
                    3 -> history_detail_type_pay.text = "Тип оплаты: Прочее"
                }

                when (historyOrderDetail.isCanceled) {
                    true -> {
                        history_detail_is_cancaled.text = "Заказан"
                        history_detail_is_cancaled.setBackgroundResource(R.color.isNotCancaled)
                    }
                    false -> {
                        history_detail_is_cancaled.text = "Отменен"
                        history_detail_is_cancaled.setBackgroundResource(R.color.isCancaled)
                    }
                }
                history_detail_date.text =
                    "Дата заказа: $historyOrderDetail.rkOrderDate.toMyDate().toMyTimezone()}"
                history_detail_paidbonus.text = "Оплачено бонусами: ${historyOrderDetail.paidBonuses}"
                history_detail_prepaid_bonus.text =
                    "Предоплачено бонусами: ${historyOrderDetail.paidPrepayPoints}"
                history_detail_all_cost.text = "Общая сумма ${historyOrderDetail.sum} сом"

                history_detail_menu_item_rv.adapter =
                    HistoryOrdersDetailAdapter(historyOrderDetail.menuItems as ArrayList<HistoryMenuItems>)
                val discount = historyOrderDetail.rkOrderDiscounts.map {response->
                    Discount(-1, response.Name, response.Sum.toInt())
                }
                history_detail_discount_item_rv.adapter = OrderDiscountAdapter(discount as ArrayList<Discount>,true)
            }

            it.doIfError {errorBody->
                errorBody?.getCrmErrors { msg->
                    toast(msg)
                }

            }
        })
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = "Заказ"
    }
}

