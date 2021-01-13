package com.timelysoft.kainarapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseListener
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseBottomSheet
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.loadingShow
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.Status
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model2.Restaurant
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.ui.MainActivity
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), RestaurantChooseListener {

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (!AppPreferences.started) {

            showRestaurantChooser()

        }
        if (AppPreferences.globalId.isNotEmpty()) {
            initMenu()
        }
        home_menu.setOnClickListener {
            findNavController().navigate(R.id.nav_food)
        }

        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
            home_basket_price.text = "$it с"
        })


/*
        home_rechoose_rest.setOnClickListener {
            showRestaurantChooser()
        }

 */



        home_discount.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("restaurantGroupId",AppPreferences.group())
            findNavController().navigate(R.id.nav_discount, bundle)
        }

        home_profile.setOnClickListener {
            findNavController().navigate(R.id.nav_profile)
        }

        home_restaurant.setOnClickListener {
            findNavController().navigate(R.id.nav_restaurant)
        }

        home_basket_price.text = "${AppPreferences.amount / 100} с"
    }

    private fun initMenu() {
        /*
        loadingShow()

        viewModel.menu().observe(viewLifecycleOwner, Observer { menu ->
            val msg = menu.msg
            val data = menu.data
            when (menu.status) {
                Status.SUCCESS -> {
                    //viewModel.updateMenu(data)
                    loadingHide(3000L)
                }
                Status.EMPTY -> {
                    loadingHide()
                }
                Status.ERROR, Status.NETWORK -> {
                    toast(msg)
                    loadingHide()

                }
            }


        })

         */
    }

    override fun onResume() {
        super.onResume()
        try {
            requireArguments().getBoolean("loading")
            loadingHide()
        } catch (e: Exception) {
            println()
        }
    }

    private fun initToolbar() {
        home_drawer.setOnClickListener {
            MainActivity.openDrawer()
        }
        home_basket.setOnClickListener {
            if (BasketCommands.listOfMenuItems.size > 0) {
                findNavController().navigate(R.id.nav_basket)
            }
        }
    }

    private fun showRestaurantChooser() {
        loadingShow()
        viewModel.restaurants().observe(viewLifecycleOwner, Observer { response ->
            loadingHide()
            response.doIfError {errorBody->
                errorBody?.getErrors {msg->
                    toast(msg)
                }

            }
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
        })
    }

    override fun onClickRestaurant(restaurantId: String, previousRestaurantId: String, crmId: Int) {
        println()
        initMenu()
    }

}
