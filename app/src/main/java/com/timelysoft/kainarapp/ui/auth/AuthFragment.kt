package com.timelysoft.kainarapp.ui.auth

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import com.timelysoft.kainarapp.ui.MainActivity
import kotlinx.android.synthetic.main.alert_sms.*
import kotlinx.android.synthetic.main.alert_sms.view.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModel()
    private var back = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initArguments()
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    private fun initArguments() {
        back = try {
            requireArguments().getBoolean("back")
        } catch (e: Exception) {
            false
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        auth_phone.addTextChangedListener {
            auth_phone.error = null
        }



        auth_sms.setOnClickListener {
            if (isValid()) {
                sendSms()
            }
        }
        auth_register.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }
    }


    private fun sendSms() {
        loadingShow()
        viewModel.sendSms(auth_phone.text.toString().toFullPhone())
            .observe(viewLifecycleOwner, Observer {
                loadingHide()
                it.doIfSuccess {success->
                    code(auth_phone.text.toString().toFullPhone())
                }
                it.doIfError {responseBody->
                    responseBody?.getCrmErrors {msg->
                        auth_phone.error = msg
                    }
                }
            })
    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }
        toolbar_text.text = getString(R.string.menu_auth)
    }

    private fun code(phone: String) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.alert_sms, null)
        builder.setView(view)
        val dialog = builder.create()
        view.sms_sent.setOnClickListener {
            if (view.sms_code.text.toString().length != 6) {
                view.sms_code.addTextChangedListener {
                    sms_code.error = null
                }
                view.sms_code.error = "Вы ввели неверный пароль"
            } else {
                loadingShow()
                viewModel.auth(phone, view.sms_code.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        loadingHide()
                        it.doIfSuccess {authResponse->
                            AppPreferences.accessToken = authResponse.accessToken
                            AppPreferences.refreshToken = authResponse.refreshToken
                            AppPreferences.isLogined = true

                            sendPushToken()

                            viewModel.profile()
                                .observe(viewLifecycleOwner, Observer { profile ->
                                    profile.doIfSuccess {profileResponse->
                                        AppPreferences.name = profileResponse.firstName
                                        AppPreferences.surname = profileResponse.lastName
                                        AppPreferences.phone = profileResponse.phoneNumber
                                    }
                                    profile.doIfError {
                                    }
                                    goTo()
                                    dialog.dismiss()
                                })

                        }
                        it.doIfError {
                            toast("Логин или пароль не верен")
                        }
                    })
            }

        }
        dialog.show()
    }

    private fun sendPushToken() {
        try {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task->
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
                                it.doIfSuccess {unit->
                                    if (unit != null){
                                        toast("Token successfully was sent")
                                    }
                                }
                                it.doIfError {errorBody->
                                    errorBody?.getCrmErrors {msg->
                                        toast(msg)
                                    }
                                }
                            })
                })

        } catch (e: Exception) {
        }
    }


    private fun goTo() {
        MainActivity.showSignOutButton(true)
        if (back) {
            findNavController().popBackStack()
        } else {
            findNavController().navigate(R.id.nav_profile)
        }

    }

    private fun isValid(): Boolean {
        var valid = true
        if (auth_phone.text!!.toString().toFullPhone().length != 12) {
            auth_phone.error = "Ввидите валидный номер"
            valid = false
        } else {
            auth_phone.error = null
        }
        return valid
    }
}
