package io.flaterlab.kyrgyzdaamy.ui.order

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
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.databinding.FragmentOrderBinding
import io.flaterlab.kyrgyzdaamy.databinding.FragmentOrderWaringDialogBinding
import io.flaterlab.kyrgyzdaamy.extension.*
import io.flaterlab.kyrgyzdaamy.service.*
import io.flaterlab.kyrgyzdaamy.service.model.AddressInfo
import io.flaterlab.kyrgyzdaamy.service.model.OrderRemote
import io.flaterlab.kyrgyzdaamy.service.response.*
import io.flaterlab.kyrgyzdaamy.ui.food.Schedules
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import io.flaterlab.kyrgyzdaamy.utils.AutoCompleteAdapter
import io.flaterlab.kyrgyzdaamy.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.bind
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order) {
    private val viewModel by viewModels<OrderViewModel>()
    private var cityId: Int = 0
    private var streetId: Int = 0
    private var addressId: Int = 0
    private var deliveryId = -1
    @Inject
    lateinit var db: FirebaseFirestore


    private val binding by viewBinding(FragmentOrderBinding::bind)


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initToolbar()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val date = Utils.convertDate(day, month + 1, year)
        val time = calendar.time.formatTo("HH:mm")

        binding.orderDate.setText(date)
        binding.orderTime.setText(time)
        binding.orderPhone.setText(AppPreferences.phoneNumber)


        binding.orderValidate.setOnClickListener {
            if (isValid()) {
                val order = with(binding) {
                    if (deliveryId == 2) {
                        OrderRemote(
                            orderName.text.toString(),
                            orderSurname.text.toString(),
                            orderPhone.text.toString(),
                            paymentTypeText.text.toString(),
                            orderDeliveryType.text.toString(),
                            orderStreets.text.toString(),
                            null,
                            null,
                            null,
                            orderComment.text.toString(),
                            orderDate.text.toString(),
                            orderTime.text.toString()
                        )
                    }else{
                        OrderRemote(
                            orderName.text.toString(),
                            orderSurname.text.toString(),
                            orderPhone.text.toString(),
                            paymentTypeText.text.toString(),
                            orderDeliveryType.text.toString(),
                            orderStreets.text.toString(),
                            orderBuilding.text.toString().toInt(),
                            orderApartmentsText.text.toString().toInt(),
                            orderFloor.text.toString().toInt(),
                            orderComment.text.toString(),
                            orderDate.text.toString(),
                            orderTime.text.toString()
                        )
                    }
                }

                val ref = db.collection("users").document(order.phoneNumber)
                ref.set(order).addOnSuccessListener {
                    BasketCommands.listHashMap.values.forEach {menuItem->
                        ref.collection("orders").document(menuItem.name).set(menuItem).addOnSuccessListener {
                            toast("Заказ оформлен, ожидайте звонка")
                            BasketCommands.deleteAllFromBasket()
                            findNavController().popBackStack(R.id.nav_basket,true)
                        }.addOnFailureListener {
                            toast(it.message)
                        }
                    }

                }.addOnFailureListener {
                    toast(it.message)
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            val paymentType = db.collection("Payment_types").get().await()
            val deliveryType = db.collection("Delivery_types").get().await()
            val paymentTypes = mutableListOf<String>()
            val deliverTypesList = mutableListOf<String>()

            paymentType.documents.forEach { snapshot ->
                paymentTypes.add(snapshot.id)
            }
            deliveryType.documents.forEach {snapshot ->
                deliverTypesList.add(snapshot.id)
            }

            val paymentTypeAdapter = ArrayAdapter(
                requireContext(),android.R.layout.simple_dropdown_item_1line,
                paymentTypes
            )
            binding.paymentTypeText.setAdapter(paymentTypeAdapter)

            val deliveryTypeAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    deliverTypesList
                )
            binding.orderDeliveryType.setAdapter(deliveryTypeAdapter)


            binding.orderDeliveryType.setOnItemClickListener { _, _, position, _ ->
                deliveryId = position + 1
                if (position == 0) {
                    setVisibility(View.VISIBLE)

                } else {
                    setVisibility(View.GONE)
                    binding.orderStreets.text = null
                    binding.orderBuilding.text = null
                    binding.orderApartmentsText.text = null
                }
            }
        }
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

        binding.orderStreets.addTextChangedListener {
            binding.orderStreetsOut.isErrorEnabled = false
        }


    }

    private fun setVisibility(visibility: Int) {
        binding.orderStreetsOut.visibility = visibility
        binding.orderBuildingOut.visibility = visibility
        binding.orderApartment.visibility = visibility
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
            if (numbers.length < 9) {
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

