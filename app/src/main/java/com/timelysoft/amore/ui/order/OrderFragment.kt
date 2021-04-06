package com.timelysoft.amore.ui.order

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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.timelysoft.amore.R
import com.timelysoft.amore.databinding.FragmentOrderBinding
import com.timelysoft.amore.databinding.FragmentOrderWaringDialogBinding
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.model.AddressInfo
import com.timelysoft.amore.service.response.*
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.food.Schedules
import com.timelysoft.amore.ui.viewBinding
import com.timelysoft.amore.utils.AutoCompleteAdapter
import com.timelysoft.amore.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order) {
    private val viewModel by viewModels<OrderViewModel>()
    private var cityId: Int = 0
    private var streetId: Int = 0
    private var addressId: Int = 0
    private var discountId = -1
    private var deliveryId = -1
    private val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())

    private val binding by viewBinding(FragmentOrderBinding::bind)


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
        binding.orderDate.setText(date)
        binding.orderDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {

                val dialog = DatePickerDialog(
                    view.context,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    { _, year, month, dayOfMonth ->
                        binding.orderDate.setText(Utils.convertDate(dayOfMonth, month + 1, year))
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
        binding.orderTime.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val dateAndTime = Calendar.getInstance()

                val t =
                    OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
                        dateAndTime[Calendar.MINUTE] = minute
                        if (minute < 10) {
                            if (hourOfDay < 10) {
                                binding.orderTime.setText("0$hourOfDay:0$minute")
                            } else {
                                binding.orderTime.setText("$hourOfDay:0$minute")
                            }
                        } else {
                            if (hourOfDay < 10) {
                                binding.orderTime.setText("0$hourOfDay:$minute")
                            } else {
                                binding.orderTime.setText("$hourOfDay:$minute")
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


        binding.orderValidate.setOnClickListener {
            if (isValid()) {
                loadingShow()
                viewModel.getBasketElements().observe(viewLifecycleOwner, { basketItems ->
                    val orderModel = Order()
                    orderModel.discountType = discountId
                    orderModel.deliveryType = deliveryId
                    val mList = mutableListOf<MenuItem>()
                    basketItems.values.forEach {
                        mList.add(it)
                    }
                    orderModel.products = convertToProductType(mList)
                    val questValidate = GuestValidate(
                        binding.orderPhone.text.toString(),
                        binding.orderName.text.toString(),
                        binding.orderSurname.text.toString(),
                        "middle",
                        AddressInfo(
                            1,
                            cityId,
                            streetId,
                            binding.orderBuilding.text.toString(),
                            binding.orderEntry.text.toString(),
                            binding.orderEntryCode.text.toString(),
                            binding.orderFloor.text.toString(),
                            binding.orderApartmentsText.text.toString(),
                            binding.orderComment.text.toString()
                        )
                    )
                    val orderValidate = OrderValidate(
                        deliveryId,
                        2,
                        convertToOrderType(
                            mList
                        ),
                        if (binding.orderDate.text.toString()
                                .isNotEmpty()
                        ) binding.orderDate.text.toString()
                            .toServerDate(binding.orderTime.text.toString()) else null
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
                                    binding.orderName.text.toString(),
                                    binding.orderPhone.text.toString(),
                                    binding.orderSurname.text.toString()
                                )
                            } else {
                                if (streetId == -1) {
                                    guestModel = Guest(
                                        AddressInfo(
                                            1,
                                            cityId,
                                            streetId,
                                            binding.orderBuilding.text.toString(),
                                            binding.orderEntry.text.toString(),
                                            binding.orderEntryCode.text.toString(),
                                            binding.orderFloor.text.toString(),
                                            binding.orderApartmentsText.text.toString(),
                                            binding.orderComment.text.toString()
                                        ),
                                        binding.orderName.text.toString(),
                                        binding.orderPhone.text.toString(),
                                        binding.orderSurname.text.toString()
                                    )
                                } else if (streetId != 0) {
                                    guestModel = Guest(
                                        AddressInfo(
                                            1,
                                            cityId,
                                            streetId,
                                            binding.orderBuilding.text.toString(),
                                            binding.orderEntry.text.toString(),
                                            binding.orderEntryCode.text.toString(),
                                            binding.orderFloor.text.toString(),
                                            binding.orderApartmentsText.text.toString(),
                                            binding.orderComment.text.toString()
                                        ),
                                        binding.orderName.text.toString(),
                                        binding.orderPhone.text.toString(),
                                        binding.orderSurname.text.toString()
                                    )
                                    if (addressId != 0) {
                                        guestModel.addressInfo?.cityId = addressId
                                    }
                                }
                            }
                            orderModel.comment = binding.orderComment.text.toString()
                            orderModel.deliverAt =
                                binding.orderDate.text.toString()
                                    .toServerDate(binding.orderTime.text.toString())

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
                        it.doIfNetwork { errorTypes ->
                            when (errorTypes) {
                                is ErrorTypes.TimeOutError -> {
                                    toast(errorTypes.msg)
                                }
                                is ErrorTypes.ConnectionError -> {
                                    toast(errorTypes.msg)
                                }
                                is ErrorTypes.EmptyResultError -> {
                                    toast(errorTypes.msg)
                                }
                            }
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

        binding.orderName.addTextChangedListener {
            binding.orderNameOut.isErrorEnabled = false
        }
        binding.orderSurname.addTextChangedListener {
            binding.orderSurnameOut.isErrorEnabled = false

        }
        binding.orderPhone.addTextChangedListener {
            binding.orderPhoneOut.isErrorEnabled = false

        }

        binding.orderBuilding.addTextChangedListener {
            binding.orderBuildingOut.isErrorEnabled = false
        }
        binding.orderDeliveryType.addTextChangedListener {
            binding.orderPayDeliveryOut.isErrorEnabled = false

        }
        binding.orderDate.addTextChangedListener {
            binding.orderDeliveryAtOut.isErrorEnabled = false
        }
        binding.orderTime.addTextChangedListener {
            binding.orderTimeOut.isErrorEnabled = false

        }
        binding.orderCities.addTextChangedListener {
            binding.orderCitiesOut.isErrorEnabled = false
        }
        binding.orderStreets.addTextChangedListener {
            binding.orderStreetsOut.isErrorEnabled = false
        }


    }

    private fun showWarning() {

        binding.orderWarningTv.setOnClickListener {
            loadingShow()
            viewModel.warning().observe(viewLifecycleOwner, { result ->
                loadingHide()
                result.doIfSuccess { response ->
                    val builder = AlertDialog.Builder(requireContext())
                    val inflater = requireActivity().layoutInflater
                    val bindingWarnings = FragmentOrderWaringDialogBinding.inflate(inflater)
                    builder.setView(view)
                    val dialog = builder.create()

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        bindingWarnings.warningMsg.text =
                            Html.fromHtml(response.data, Html.FROM_HTML_MODE_LEGACY).trim()

                    } else {
                        bindingWarnings.warningMsg.text = Html.fromHtml(response.data)

                    }

                    bindingWarnings.warningYes.setOnClickListener {
                        binding.orderWarningCheckbox.isChecked = true
                        dialog.dismiss()
                    }
                    bindingWarnings.warningNo.setOnClickListener {
                        binding.orderWarningCheckbox.isChecked = false
                        dialog.dismiss()
                    }

                    dialog.show()
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
        val orderType = listOf("Самовывоз", "Доставка")
        val payTypeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                orderType
            )
        binding.orderDeliveryType.setAdapter(payTypeAdapter)

        if (addressId != 0 || cityId != 0) {
            binding.orderStreetsOut.visibility = View.VISIBLE
            binding.orderCitiesOut.visibility = View.VISIBLE
            binding.orderBuildingOut.visibility = View.VISIBLE
            binding.orderApartment.visibility = View.VISIBLE
        }
        binding.orderDeliveryType.setOnItemClickListener { _, _, position, _ ->
            deliveryId = position + 1
            if (position == 0) {
                cityId = 0
                streetId = 0
                setVisibility(View.GONE)
                binding.orderCities.text = null
                binding.orderStreets.text = null
                binding.orderBuilding.text = null
                binding.orderApartmentsText.text = null
            } else {
                setVisibility(View.VISIBLE)
            }
        }
    }

    private fun setVisibility(visibility: Int) {
        binding.orderStreetsOut.visibility = visibility
        binding.orderCitiesOut.visibility = visibility
        binding.orderBuildingOut.visibility = visibility
        binding.orderApartment.visibility = visibility
        binding.orderEntryCodeOut.visibility = visibility
        binding.orderEntryOut.visibility = visibility
        binding.orderFloorOut.visibility = visibility
    }


    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            binding.toolbar.toolbarBack.visibility = View.VISIBLE
        }
        binding.toolbar.toolbarText.text = getString(R.string.menu_order)
        binding.toolbar.toolbarBack.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }


    private fun getAutoCities() {
        loadingShow()
        viewModel.cities().observe(viewLifecycleOwner, { result ->
            loadingHide()
            result.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    toast(msg)
                }
            }
            result.doIfSuccess {

                val adapterAddress =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it.data
                    )
                binding.orderCities.setAdapter(adapterAddress)
            }
            result.doIfNetwork { errorType ->
                when (errorType) {
                    is ErrorTypes.TimeOutError -> {
                        toast(errorType.msg)
                    }
                    is ErrorTypes.ConnectionError -> {
                        toast(errorType.msg)
                    }
                    is ErrorTypes.EmptyResultError -> {
                        toast(errorType.msg)
                    }
                }

            }

        })


        binding.orderCities.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = binding.orderCities.adapter.getItem(position) as CityRestResponse
                cityId = item.id
                streetId = 0
                addressId = 0
                binding.orderCities.showDropDown()
                getAutoStreets()

                binding.orderStreets.apply {
                    setAdapter(null)
                    setText("")
                }
            }
    }


    private fun getAutoStreets() {
        loadingShow()
        viewModel.streets(cityId).observe(viewLifecycleOwner, { result ->
            loadingHide()

            result.doIfNetwork { errorType ->
                when (errorType) {
                    is ErrorTypes.TimeOutError -> {
                        toast(errorType.msg)
                    }
                    is ErrorTypes.ConnectionError -> {
                        toast(errorType.msg)
                    }
                    is ErrorTypes.EmptyResultError -> {
                        toast(errorType.msg)
                    }
                }
            }
            result.doIfSuccess { streetResponse ->
                val listOfResponse = mutableListOf<StreetResponse>()
                streetResponse.data.forEach {
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
                binding.orderStreets.setAdapter(a)
                binding.orderStreets.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val item =
                            binding.orderStreets.adapter.getItem(position) as StreetResponse
                        streetId = item.id
                        addressId = 0
                        if (streetId == -1) {
                            binding.orderBuildingOut.hint = "Введите адрес"
                            binding.orderApartment.hint = "Введите адрес"
                        } else {
                            binding.orderBuildingOut.hint = "Дом"
                            binding.orderApartment.hint = "Квартира"
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

        if (!validateField(binding.orderName.text.toString(), binding.orderNameOut)) {
            valid = false
        }

        if (!validateField(binding.orderSurname.text.toString(), binding.orderSurnameOut)) {
            valid = false
        }
        if (!validateField(binding.orderPhone.text.toString(), binding.orderPhoneOut)) {
            valid = false
        }
        if (!validateField(
                binding.orderDeliveryType.text.toString(),
                binding.orderPayDeliveryOut
            )
        ) {
            valid = false
        }
        if (!validateField(binding.orderTime.text.toString(), binding.orderTimeOut)) {
            valid = false
        }
        if (!validateField(binding.orderDate.text.toString(), binding.orderDeliveryAtOut)) {
            valid = false
        }
        if (binding.orderPhone.text.toString().isNotEmpty()) {
            val phoneNumber = binding.orderPhone.text.toString()
            val numbers = phoneNumber.filter {
                it.isDigit()
            }
            if (numbers.length < 11) {
                binding.orderPhoneOut.error = "Введите номер полностью"
                valid = false
            } else {
                binding.orderPhoneOut.isErrorEnabled = false
            }
        }

        if (cityId != 0) {
            if (!validateField(binding.orderBuilding.text.toString(), binding.orderBuildingOut)) {
                valid = false
            }
        }
        if (deliveryId == 2) {
            if (addressId == 0) {
                if (!validateField(binding.orderCities.text.toString(), binding.orderCitiesOut)) {
                    valid = false
                } else {
                    binding.orderCitiesOut.isErrorEnabled = false
                }

                if (!validateField(binding.orderStreets.text.toString(), binding.orderStreetsOut)) {
                    valid = false
                } else {
                    binding.orderStreetsOut.isErrorEnabled = false
                }
            }
        }
        if (binding.orderTime.text!!.isNotEmpty() && binding.orderDate.text!!.isNotEmpty()) {
            val currentDayView = binding.orderDate.text?.substring(0, 2)?.toInt()
            Log.d("CurrentDayView", currentDayView.toString())
            val currentMonthView = binding.orderDate.text?.substring(3, 5)?.toInt()
            Log.d("CurrentMonthView", currentMonthView.toString())

            val currentDayAndroid = calendar.get(Calendar.DAY_OF_MONTH)
            Log.d("CurrentDayAndroid", currentDayAndroid.toString())

            val currentMonthAndroid = calendar.get(Calendar.MONTH) + 1
            Log.d("CurrentMonthAndroid", currentMonthAndroid.toString())

            val date = Calendar.getInstance().time

            val currentTime = date.formatTo("HH:mm").convertToMin(DateFromTo.DateTo)

            val time = binding.orderTime.text.toString()

            val hour = time.convertToMin(DateFromTo.DateFrom)

            val minusThirtyTime = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo) - 30

            val closedTime = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo)


            if (currentDayView != currentDayAndroid || currentMonthView != currentMonthAndroid) {
                Log.d("CurrentDayNo", "dasd")
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.DAY_OF_MONTH] = currentDayView!!
                calendar1[Calendar.MONTH] = currentMonthView!!
                val dayOfWeek = calendar1[Calendar.DAY_OF_WEEK]
                val schedule = Schedules.scheduleList.find {
                    it.dayOfWeek == dayOfWeek
                }
                schedule?.let {
                    Log.d("Schedule", "NotNull")

                    val dateFrom = schedule.dateFrom.toHour().toDate()!!.formatTo("HH:mm")
                    val dateTo = schedule.dateTo.toHour().toDate()!!.formatTo("HH:mm")

                    if (hour in schedule.dateFrom.toHour().toDate()!!.formatTo("HH:mm")
                            .convertToMin(DateFromTo.DateFrom)
                            .rangeTo(closedTime)
                    ) {

                        binding.orderTimeOut.isErrorEnabled = false
                    } else {

                        binding.orderTimeOut.error = "График: $dateFrom-$dateTo"
                        valid = false
                    }
                } ?: Log.d("Schedule", "Null")


            } else {
                Log.d("CurrentDAY: ", "+")
                if (currentTime + 60 <= hour) {
                    Log.d("CurrentDAYINCOND: ", "+")

                    if (hour > closedTime) {
                        valid = false
                        //val time = AppPreferences.dateTo!!.convertToMin(DateFromTo.DateTo) - 30
                        //order_time_out.error = "Заказы принимаются до ${time.toHour()}"
                        binding.orderTimeOut.error =
                            "Ресторан закрывается в ${AppPreferences.dateTo}"
                    } else {
                        if (currentTime in minusThirtyTime..closedTime) {
                            valid = false
                            binding.orderTimeOut.error = "Заказы принимаются на пол часа раньше"
                        } else {
                            if (hour <= closedTime) {
                                binding.orderTimeOut.isErrorEnabled = false
                            } else {
                                binding.orderTimeOut.error =
                                    "Ресторан закрывается в ${AppPreferences.dateTo}"
                                valid = false

                            }
                        }

                    }
                } else {
                    Log.d("CurrentDAYINCOND: ", "-")
                    binding.orderTimeOut.error = "Минимум на час позже чем сейчас"
                    valid = false
                }
            }

        }

        if (!binding.orderWarningCheckbox.isChecked) {
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
