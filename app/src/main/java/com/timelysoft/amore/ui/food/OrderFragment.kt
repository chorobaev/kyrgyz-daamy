package com.timelysoft.amore.ui.food

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.timelysoft.amore.R
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.model.AddressInfo
import com.timelysoft.amore.service.response.*
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.utils.AutoCompleteAdapter
import com.timelysoft.amore.utils.Utils
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order_waring_dialog.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class OrderFragment : Fragment() {
    private val viewModel: FoodViewModel by viewModel()
    private var cityId: Int = 0
    private var streetId: Int = 0
    private var addressId: Int = 0
    private var discountId = -1
    private var deliveryId = -1
    private val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order, container, false)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initToolbar()
        getAutoCities()
        showWarning()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val date = Utils.convertDate(day, month + 1, year)
        order_date.setText(date)
        order_date.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {

                val dialog = DatePickerDialog(
                    view.context,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    { _, year, month, dayOfMonth ->
                        order_date.setText(Utils.convertDate(dayOfMonth, month + 1, year))
                    },
                    year,
                    month,
                    day
                )
                dialog.datePicker.minDate = System.currentTimeMillis() - 1000
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

            }
            false
        }
        order_time.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val dateAndTime = Calendar.getInstance()

                val t =
                    OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
                        dateAndTime[Calendar.MINUTE] = minute
                        if (minute < 10) {
                            if (hourOfDay < 10) {
                                order_time.setText("0$hourOfDay:0$minute")
                            } else {
                                order_time.setText("$hourOfDay:0$minute")
                            }
                        } else {
                            if (hourOfDay < 10) {
                                order_time.setText("0$hourOfDay:$minute")
                            } else {
                                order_time.setText("$hourOfDay:$minute")
                            }
                        }

                    }


                val timePickerDialog = TimePickerDialog(
                    activity, t,
                    dateAndTime[Calendar.HOUR_OF_DAY],
                    dateAndTime[Calendar.MINUTE], true
                )
                timePickerDialog.show()

            }

            false
        }


        order_validate.setOnClickListener {
            if (isValid()) {
                loadingShow()
                viewModel.getBasketElements().observe(viewLifecycleOwner, Observer { basketItems ->
                    val orderModel = Order()
                    orderModel.discountType = discountId
                    orderModel.deliveryType = deliveryId
                    orderModel.products = convertToProductType(basketItems)
                    val questValidate = GuestValidate(
                        order_phone.text.toString(),
                        order_name.text.toString(),
                        order_surname.text.toString(),
                        "middle",
                        AddressInfo(
                            1,
                            cityId,
                            streetId,
                            order_building.text.toString(),
                            order_entry.text.toString(),
                            order_entry_code.text.toString(),
                            order_floor.text.toString(),
                            order_apartments_text.text.toString(),
                            order_comment.text.toString()
                        )
                    )
                    val orderValidate = OrderValidate(
                        deliveryId, 2, convertToOrderType(
                            basketItems
                        ), if (order_date.text.toString().isNotEmpty()) order_date.text.toString()
                            .toServerDate(order_time.text.toString()) else null
                    )

                    val validateOrder = ValidateOrder(questValidate, orderValidate)

                    viewModel.orderValidate(validateOrder).observe(viewLifecycleOwner, Observer {
                        loadingHide()

                        it.doIfSuccess {
                            val bundle = Bundle()

                            var guestModel = Guest()
                            if (deliveryId == 1) {
                                guestModel = Guest(
                                    null,
                                    order_name.text.toString(),
                                    order_phone.text.toString(),
                                    order_surname.text.toString()
                                )
                            } else {
                                if (streetId == -1) {
                                    guestModel = Guest(
                                        AddressInfo(
                                            1,
                                            cityId,
                                            streetId,
                                            order_building.text.toString(),
                                            order_entry.text.toString(),
                                            order_entry_code.text.toString(),
                                            order_floor.text.toString(),
                                            order_apartments_text.text.toString(),
                                            order_comment.text.toString()
                                        ),
                                        order_name.text.toString(),
                                        order_phone.text.toString(),
                                        order_surname.text.toString()
                                    )
                                } else if (streetId != 0) {
                                    guestModel = Guest(
                                        AddressInfo(
                                            1,
                                            cityId,
                                            streetId,
                                            order_building.text.toString(),
                                            order_entry.text.toString(),
                                            order_entry_code.text.toString(),
                                            order_floor.text.toString(),
                                            order_apartments_text.text.toString(),
                                            order_comment.text.toString()
                                        ),
                                        order_name.text.toString(),
                                        order_phone.text.toString(),
                                        order_surname.text.toString()
                                    )
                                    if (addressId != 0) {
                                        guestModel.addressInfo?.cityId = addressId
                                    }
                                }
                            }
                            orderModel.comment = order_comment.text.toString()
                            orderModel.deliverAt =
                                order_date.text.toString()
                                    .toServerDate(order_time.text.toString())

                            val createOrder = CreateOrder(guestModel, orderModel)
                            bundle.putParcelable("result", it as OrderValidateResponse)
                            bundle.putParcelable("order", createOrder)
                            findNavController().navigate(R.id.nav_order_detail, bundle)
                        }

                        it.doIfError { errorBody ->

                            errorBody?.getErrors { msg ->
                                toast(msg)
                            }
                        }
                        it.doIfNetwork { msg ->
                            toast(msg)
                        }


                    })
                })
            }
        }
    }

    private fun convertToProductType(list: List<MenuItem>): List<Product> {
        val mutableList = mutableListOf<Product>()
        var listTotal: MutableList<BaseModifier>
        var listOfModOrderState: MutableList<Modifier>
        list.forEach { menuItem ->
            listTotal = ArrayList()
            menuItem.modifierGroups.forEachIndexed { index, baseModifierGroup ->
                baseModifierGroup.modifiersList.forEach {
                    listTotal.add(it.value)
                }
                //listTotal.addAll(baseModifierGroup.modifiersList.values)
            }
            listOfModOrderState = ArrayList()
            listTotal.forEach {
                val modifierOrderState = Modifier(it.code, it.count, it.name)
                listOfModOrderState.add(modifierOrderState)
            }
            if (menuItem.modifierGroups.isNullOrEmpty()) {
                val productOrderState = Product(
                    menuItem.code,
                    menuItem.amount,
                    menuItem.name
                )
                mutableList.add(productOrderState)
            } else {
                val productOrderState = Product(
                    menuItem.code,
                    menuItem.amount,
                    menuItem.name,
                    listOfModOrderState
                )
                mutableList.add(productOrderState)
            }

        }

        return mutableList
    }

    private fun convertToOrderType(list: List<MenuItem>): List<ProductOrderState> {

        val mutableList = mutableListOf<ProductOrderState>()
        var listTotal: MutableList<BaseModifier>
        var listOfModOrderState: MutableList<ModifierOrderState>
        list.forEach { menuItem ->
            listTotal = ArrayList()
            menuItem.modifierGroups.forEachIndexed { index, baseModifierGroup ->
                baseModifierGroup.modifiersList.forEach {
                    listTotal.add(it.value)
                }
            }
            listOfModOrderState = ArrayList()
            listTotal.forEach {
                val modifierOrderState = ModifierOrderState(it.code, it.count, it.name)
                listOfModOrderState.add(modifierOrderState)
            }
            if (menuItem.modifierGroups.isNullOrEmpty()) {
                val productOrderState = ProductOrderState(
                    menuItem.code,
                    menuItem.amount,
                    menuItem.name,
                    menuItem.priceWithMod,
                    menuItem.price
                )
                mutableList.add(productOrderState)
            } else {
                val productOrderState = ProductOrderState(
                    menuItem.code,
                    menuItem.amount,
                    menuItem.name,
                    menuItem.priceWithMod,
                    menuItem.price,
                    listOfModOrderState

                )
                mutableList.add(productOrderState)
            }
        }
        return mutableList
    }


    private fun initViews() {

        order_name.addTextChangedListener {
            order_name_out.isErrorEnabled = false
        }
        order_surname.addTextChangedListener {
            order_surname_out.isErrorEnabled = false

        }
        order_phone.addTextChangedListener {
            order_phone_out.isErrorEnabled = false

        }
        order_type.addTextChangedListener {
            order_type_out.isErrorEnabled = false

        }
        order_building.addTextChangedListener {
            order_building_out.isErrorEnabled = false
        }
        order_delivery_type.addTextChangedListener {
            order_pay_delivery_out.isErrorEnabled = false

        }
        order_date.addTextChangedListener {
            order_deliveryAt_out.isErrorEnabled = false
        }
        order_time.addTextChangedListener {
            order_time_out.isErrorEnabled = false

        }
        order_cities.addTextChangedListener {
            order_cities_out.isErrorEnabled = false
        }
        order_streets.addTextChangedListener {
            order_streets_out.isErrorEnabled = false
        }


    }

    private fun showWarning() {

        order_warning_tv.setOnClickListener {
            loadingShow()
            viewModel.warning().observe(viewLifecycleOwner, Observer { result ->
                loadingHide()
                result.doIfSuccess { response ->
                    if (response != null) {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = requireActivity().layoutInflater
                        val view = inflater.inflate(R.layout.fragment_order_waring_dialog, null)
                        builder.setView(view)
                        val dialog = builder.create()

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            view.warning_msg.text =
                                Html.fromHtml(response, Html.FROM_HTML_MODE_LEGACY).trim()

                        } else {
                            view.warning_msg.text = Html.fromHtml(response)

                        }

                        view.warning_yes.setOnClickListener {
                            order_warning_checkbox.isChecked = true
                            dialog.dismiss()
                        }
                        view.warning_no.setOnClickListener {
                            order_warning_checkbox.isChecked = false
                            dialog.dismiss()
                        }

                        dialog.show()
                    }
                }

                result.doIfError {
                    it?.getErrors { msg ->
                        toast(msg)
                    }

                }

            })
        }
    }

    override fun onStart() {
        super.onStart()

        val discountType = listOf("Бонус", "Скидка")
        val discountTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            discountType
        )
        order_type.setAdapter(discountTypeAdapter)
        order_type.setOnItemClickListener { _, _, position, _ ->
            discountId = (position + 1)
        }

        val orderType = listOf("Самовывоз", "Доставка")
        val payTypeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                orderType
            )
        order_delivery_type.setAdapter(payTypeAdapter)

        if (addressId != 0 || cityId != 0) {
            order_list_addresses_out.visibility = View.VISIBLE
            order_streets_out.visibility = View.VISIBLE
            order_cities_out.visibility = View.VISIBLE
            order_building_out.visibility = View.VISIBLE
            order_apartment.visibility = View.VISIBLE
        }
        order_delivery_type.setOnItemClickListener { _, _, position, _ ->
            deliveryId = position + 1
            if (position == 0) {
                cityId = 0
                streetId = 0
                setVisibility(View.GONE)
                order_cities.text = null
                order_streets.text = null
                order_building.text = null
                order_apartments_text.text = null
                order_list_addresses.text = null
            } else {
                setVisibility(View.VISIBLE)
            }
        }
    }

    private fun setVisibility(visibility: Int) {
        order_streets_out.visibility = visibility
        order_cities_out.visibility = visibility
        order_building_out.visibility = visibility
        order_apartment.visibility = visibility
        order_entryCode_out.visibility = visibility
        order_entry_out.visibility = visibility
        order_floor_out.visibility = visibility
    }


    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            toolbar_back.visibility = View.VISIBLE
        }
        toolbar_text.text = getString(R.string.menu_order)
        toolbar_back.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }


    private fun getAutoCities() {
        loadingShow()
        viewModel.cities().observe(viewLifecycleOwner, Observer { result ->
            loadingHide()
            result.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    toast(msg)
                }
            }
            result.doIfSuccess {
                val adapterAddress = it?.let { it1 ->
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it1
                    )
                }
                order_cities.setAdapter(adapterAddress)
            }
            result.doIfNetwork { msg ->
                toast(msg)
            }
        })

        order_cities.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = order_cities.adapter.getItem(position) as CityRestResponse
                cityId = item.id
                streetId = 0
                addressId = 0
                order_cities.showDropDown()
                getAutoStreets()

                order_streets.setAdapter(null)
                order_streets.setText("")
                order_list_addresses.text = null
            }
    }


    private fun getAutoStreets() {
        loadingShow()
        viewModel.streets(cityId).observe(viewLifecycleOwner, Observer { result ->
            loadingHide()

            result.doIfNetwork { msg ->
                toast(msg)
            }
            result.doIfSuccess { streetResponse ->
                val listOfResponse = mutableListOf<StreetResponse>()
                streetResponse?.forEach {
                    listOfResponse.add(it)
                }
                listOfResponse.add(
                    StreetResponse(
                        cityName = "Bishkek",
                        id = -1,
                        name = "Указать свой адрес..."
                    )
                )
                val a = AutoCompleteAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    items = listOfResponse
                )
                order_streets.setAdapter(a)
                order_streets.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val item =
                            order_streets.adapter.getItem(position) as StreetResponse
                        streetId = item.id
                        addressId = 0
                        if (streetId == -1) {
                            order_building_out.hint = "Введите адрес"
                            order_apartment.hint = "Введите адрес"
                        } else {
                            order_building_out.hint = "Дом"
                            order_apartment.hint = "Квартира"
                        }
                    }
            }

            result.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    toast(msg)
                }
            }

        })


    }

    private fun isValid(): Boolean {
        var valid = true

        if (!validateField(order_name.text.toString(), order_name_out)) {
            valid = false
        }

        if (!validateField(order_surname.text.toString(), order_surname_out)) {
            valid = false
        }
        if (!validateField(order_phone.text.toString(), order_phone_out)) {
            valid = false
        }
        if (!validateField(order_delivery_type.text.toString(), order_pay_delivery_out)) {
            valid = false
        }
        if (!validateField(order_time.text.toString(), order_time_out)) {
            valid = false
        }
        if (!validateField(order_date.text.toString(), order_deliveryAt_out)) {
            valid = false
        }
        if (order_phone.text.toString().isNotEmpty()) {
            val phoneNumber = order_phone.text.toString()
            val numbers = phoneNumber.filter {
                it.isDigit()
            }
            if (numbers.length < 11) {
                order_phone_out.error = "Введите номер полностью"
                valid = false
            } else {
                order_phone_out.isErrorEnabled = false
            }
        }

        if (cityId != 0) {
            if (!validateField(order_building.text.toString(), order_building_out)) {
                valid = false
            }
        }
        if (deliveryId == 2) {
            if (addressId == 0) {
                if (!validateField(order_cities.text.toString(), order_cities_out)) {
                    valid = false
                } else {
                    order_cities_out.isErrorEnabled = false
                }

                if (!validateField(order_streets.text.toString(), order_streets_out)) {
                    valid = false
                } else {
                    order_streets_out.isErrorEnabled = false
                }
            } else {
                if (!validateField(
                        order_list_addresses.text.toString(),
                        order_list_addresses_out
                    )
                ) {
                    valid = false
                } else {
                    order_list_addresses_out.isErrorEnabled = false
                }
            }
        }
        if (order_time.text!!.isNotEmpty() && order_date.text!!.isNotEmpty()) {
            val currentDayView = order_date.text?.substring(0, 2)?.toInt()
            Log.d("CurrentDayView",currentDayView.toString())
            val currentMonthView = order_date.text?.substring(3, 5)?.toInt()
            Log.d("CurrentMonthView",currentMonthView.toString())

            val currentDayAndroid = calendar.get(Calendar.DAY_OF_MONTH)
            Log.d("CurrentDayAndroid",currentDayAndroid.toString())

            val currentMonthAndroid = calendar.get(Calendar.MONTH)+1
            Log.d("CurrentMonthAndroid",currentMonthAndroid.toString())

            val date = Calendar.getInstance().time

            val currentTime = date.formatTo("HH:mm").convertToMin(DateFromTo.DateTo)

            val time = order_time.text.toString()

            val hour = time.convertToMin(DateFromTo.DateFrom)

            val minusThirtyTime = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo) - 30

            val closedTime = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo)


            if (currentDayView != currentDayAndroid || currentMonthView != currentMonthAndroid) {
                Log.d("CurrentDayNo","dasd")
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.DAY_OF_MONTH] = currentDayView!!
                calendar1[Calendar.MONTH] = currentMonthView!!
                val dayOfWeek = calendar1[Calendar.DAY_OF_WEEK]
                val schedule = Schedules.scheduleList.find {
                    it.dayOfWeek == dayOfWeek
                }
                schedule?.let {
                    Log.d("Schedule","NotNull")

                    val dateFrom = schedule.dateFrom.toHour().toDate()!!.formatTo("HH:mm")
                    val dateTo = schedule.dateTo.toHour().toDate()!!.formatTo("HH:mm")

                    if (hour in schedule.dateFrom.toHour().toDate()!!.formatTo("HH:mm")
                            .convertToMin(DateFromTo.DateFrom)
                            .rangeTo(closedTime)
                    ) {

                        order_time_out.isErrorEnabled = false
                    } else {

                        order_time_out.error = "График: $dateFrom-$dateTo"
                        valid = false
                    }
                }?: Log.d("Schedule","Null")



            } else {
                Log.d("CurrentDAY: ","+")
                if (currentTime + 60 <= hour) {
                    Log.d("CurrentDAYINCOND: ","+")

                    if (hour > closedTime) {
                        valid = false
                        //val time = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo) - 30
                        //order_time_out.error = "Заказы принимаются до ${time.toHour()}"
                        order_time_out.error = "Ресторан закрывается в ${AppPreferences.dateTo}"
                    } else {
                        if (currentTime in minusThirtyTime..closedTime) {
                            valid = false
                            order_time_out.error = "Заказы принимаются на пол часа раньше"
                        } else {
                            if (hour <= closedTime) {
                                order_time_out.isErrorEnabled = false
                            } else {
                                    order_time_out.error =
                                        "Ресторан закрывается в ${AppPreferences.dateTo}"
                                    valid = false

                            }
                        }

                    }
                } else {
                    Log.d("CurrentDAYINCOND: ","-")
                    order_time_out.error = "Минимум на час позже чем сейчас"
                    valid = false
                }
            }

        }

        if (!order_warning_checkbox.isChecked) {
            toast("Без соглашения не можете заказать")
            valid = false
        }
        return valid

    }

    private fun validateField(input: String, textInputLayout: TextInputLayout): Boolean {
        if (input.isEmpty()) {
            textInputLayout.error = requireContext().getString(R.string.validation)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }
}
