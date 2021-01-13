package com.timelysoft.kainarapp.ui.auth

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.*
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class ProfileFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initListeners()


        if (!AppPreferences.isLogined) {
            val navBuilder = NavOptions.Builder()
            val navOptions = navBuilder.setPopUpTo(R.id.nav_home,false).build()
            findNavController().navigate(R.id.nav_auth,null,navOptions)
        } else {
            loadProfile()
            loadRestaurants()

        }


    }

    private fun initListeners() {
        profile_exit.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.out)
            builder.setMessage(R.string.confirm_sign_out)
            builder.setPositiveButton(R.string.sign_out) { _, _ ->
                AppPreferences.clear()
                findNavController().navigate(R.id.nav_home)
            }

            builder.setNegativeButton(R.string.cancel) { _, _ ->

            }


            val dialog = builder.create();
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
            }

            dialog.show()
        }
    }

    private fun loadRestaurants() {
        viewModel.restaurantsCRM().observe(viewLifecycleOwner, Observer {restaurants->

            restaurants.doIfSuccess {restaurantsResponseCRM->
                val restaurantAdapter = restaurantsResponseCRM?.let { it1 ->
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it1
                    )
                }
                profile_restaurants.setAdapter(restaurantAdapter)

                profile_restaurants.setOnItemClickListener { _, _, position, _ ->
                    loadingShow()
                    if (restaurantsResponseCRM != null) {
                        viewModel.profileAccount(restaurantsResponseCRM[position].crmId)
                            .observe(viewLifecycleOwner, Observer { profile ->
                                loadingHide()
                                profile.doIfSuccess {
                                    profile_info.visibility = View.VISIBLE
                                    profile_bonus.setText(it.bonusPoints.toString())
                                    profile_discount.setText(it.cashbackName)
                                }
                                profile.doIfError {
                                    it?.getCrmErrors {msg->
                                        toast(msg)
                                    }
                                }
                            })
                    }
                }
            }

            restaurants.doIfError {
                it?.getErrors {msg->
                    toast(msg)
                }
            }

            restaurants.doIfNetwork {
                toast(it)
            }

        })
    }

    private fun loadProfile() {
        loadingShow()
        viewModel.profile().observe(viewLifecycleOwner, Observer {
            loadingHide()

            it.doIfSuccess {profileResponse->
                profile_name.setText(profileResponse.firstName)
                profile_surname.setText(profileResponse.lastName)
                profile_phone.setText(profileResponse.phoneNumber)
                try {
                    profile_age.setText(profileResponse.birthday.toMyDate())
                } catch (e:Exception){

                }

                profile_code_bonus.setText(profileResponse.bonusPhysicalCardCode.toString())
                profile_discount_physical.setText(profileResponse.discountPhysicalCardCode.toString())
                profile_discount_discount_name.setText(profileResponse.discountName)
                profile_discount_gender.setText(profileResponse.sexName)

                AppPreferences.name = profileResponse.firstName
                AppPreferences.surname = profileResponse.lastName
                AppPreferences.phone = profileResponse.phoneNumber
            }

            it.doIfError {response->
                response?.getCrmErrors {msg->
                    toast(msg)
                }
            }



        })
    }


    //qr code
//    private fun initData() {
//        try {
//            val multiFormatWriter = MultiFormatWriter()
//            val bitMatrix =
//                multiFormatWriter.encode("Beksar", BarcodeFormat.QR_CODE, 200, 200)
//            val barcodeEncoder = BarcodeEncoder()
//            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
//            profile_qr.setImageBitmap(bitmap)
//        } catch (e: Exception) {
//
//        }
//    }

    private fun initToolbar() {
        back()
        profile_back.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }
    }


    private fun back() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_home)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}
