package com.timelysoft.kainarapp.ui.auth

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.Status
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model.FirebaseTokenModel
import com.timelysoft.kainarapp.service.model.RegisterModel
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import com.timelysoft.kainarapp.ui.MainActivity
import com.timelysoft.kainarapp.utils.Utils
import kotlinx.android.synthetic.main.alert_sms.view.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.success_payment_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class RegisterFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewGenders()
        initListeners()



//        register_gender.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                val genders =
//                    arrayOf(getText(R.string.language_male), getText(R.string.language_female))
//                val builder = AlertDialog.Builder(context)
//                builder.setTitle(R.string.choose_gender)
//                var dialog = builder.create()
//                builder.setSingleChoiceItems(genders, -1) { _, which ->
//                    register_gender.setText(genders[which])
//                    this.sexPosition = which
//                    dialog.dismiss()
//                }
//                dialog = builder.create()
//                dialog.show()
//            }
//            false
//        }


//        register_name.addTextChangedListener {
////            register_name.error = null
//            register_name.error = null
//        }
//
//        register_surname.addTextChangedListener {
//            register_surname.error = null
//        }

//        register_gender.addTextChangedListener {
//            register_gender.error = null
//        }


//        register_birthday.addTextChangedListener {
//            register_birthday.error = null
//        }

//        register_phone_number.addTextChangedListener {
//            register_phone_number.error = null
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        register_birthday.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())

                val dialog = DatePickerDialog(
                    requireView().context,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    { _, year, month, dayOfMonth ->
                        register_birthday.setText(Utils.convertDate(dayOfMonth, month + 1, year))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
//                    order_date.setText(Utils.convertDate(dayOfMonth, month + 1, year))
//                }
                dialog.show()

            }
            false
        }

        register_register.setOnClickListener {
            if (isValid()) {
                val register = RegisterModel()
                register.phoneNumber = register_phone_number.text.toString().toFullPhone()
                register.firstName = register_name.text.toString()
                register.lastName = register_surname.text.toString()

                register.birthday = if (register_birthday.text.toString()
                        .isNotEmpty()
                ) register_birthday.text.toString().toServerDate() else null
                register.sex =
                    if (register_gender_group.checkedRadioButtonId != -1) register_gender_group.checkedRadioButtonId else 0


                loadingShow()
                viewModel.register(register).observe(viewLifecycleOwner, Observer { result ->
                    loadingHide()

                    result.doIfSuccess {
                        code(register.phoneNumber)
                    }
                    result.doIfError {errorBody->
                        errorBody?.getCrmErrors {msg->
                            register_phone_number.error = msg
                        }
                    }
                })
            }
        }
    }

    private fun initViewGenders() {
        for (i in 1..2) {
            val child = RadioButton(context)
            child.id = i
            if (i == 1) {
                child.text = "Мужской"
            } else {
                child.text = "Женский"
            }

            register_gender_group.addView(child)
        }
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_register)
    }

    private fun code(phone: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.alert_sms, null)
        builder.setView(view)
        val dialog = builder.create()
        view.sms_sent.setOnClickListener {
            if (view.sms_code.text!!.toString().length != 6) {
                view.sms_code.error = "Вы ввели неверный пароль"
            } else {
                loadingShow()
                viewModel.auth(phone, view.sms_code.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        loadingHide()
                        it.doIfError {errorBody->
                           errorBody?.getCrmErrors {msg->
                               toast(msg)
                           }
                        }

                        it.doIfSuccess {authResponse->
                            AppPreferences.accessToken = authResponse.accessToken
                            AppPreferences.refreshToken = authResponse.refreshToken
                            AppPreferences.isLogined = true

                            try {
                                FirebaseInstanceId.getInstance().instanceId
                                    .addOnCompleteListener(OnCompleteListener { task ->
                                        if (!task.isSuccessful) {
                                            return@OnCompleteListener
                                        }
                                        val token = task.result?.token
                                        if (token != null)
                                            viewModel.sendToken(
                                                FirebaseTokenModel(
                                                    token,
                                                    0
                                                )
                                            ).observe(viewLifecycleOwner,
                                                Observer {
                                                    MainActivity.showSignOutButton(true)
                                                    findNavController().navigate(R.id.nav_home)
                                                })
                                    })
                            } catch (e: Exception) {


                            }
                            dialog.dismiss()
                        }
                    })
            }
        }
        dialog.show()
    }

    private fun isValid(): Boolean {
        var valid = true
        if (register_name.text.toString().isEmpty()) {
            register_name.error = "Поле не должно быть пустым"
            valid = false
        } else {
            register_name.error = null
        }

        if (register_surname.text.toString().isEmpty()) {
            register_surname.error = "Поле не должно быть пустым"
            valid = false
        } else {
            register_surname.error = null
        }
//
//        if (register_gender.text.toString().isEmpty()) {
//            register_gender.error = "Выберите пол"
//            valid = false
//        } else {
//            register_gender.error = null
//        }


//        if (register_birthday.text.toString().isEmpty()) {
//            register_birthday.error = "Поле не должно быть пустым"
//            valid = false
//        } else {
//            register_birthday.error = null
//        }

        if (register_phone_number.text!!.toString().toFullPhone().length != 12) {
            register_phone_number.error = "Ввидите валидный номер"
            valid = false
        } else {
            register_phone_number.error = null
        }
        return valid
    }
}
