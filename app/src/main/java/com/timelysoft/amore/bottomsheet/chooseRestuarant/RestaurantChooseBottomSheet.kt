package com.timelysoft.amore.bottomsheet.chooseRestuarant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.restaurant.RestaurantAdapter
import com.timelysoft.amore.adapter.restaurant.RestaurantListener
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.model2.RestaurantResponse
import kotlinx.android.synthetic.main.fragment_restaurant_choose_bs.*


class RestaurantChooseBottomSheet(
    private val listener: RestaurantChooseListener,
    private val items: ArrayList<RestaurantResponse> = ArrayList()
) : BottomSheetDialogFragment(), RestaurantListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_restaurant_choose_bs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }

    private fun initViews() {
        choose_restaurant_rv.adapter = RestaurantAdapter(this, items, true)

    }


    override fun onRestaurantClick(
        restaurantId: String,
        photo: String,
        logo: String,
        crmId: Int
    ) {

        val previousRestaurantId = AppPreferences.restaurant
            if (!AppPreferences.change){
            AppPreferences.restaurant = restaurantId
            AppPreferences.started = true
            AppPreferences.restaurantPhoto = photo
            AppPreferences.restaurantLogo = logo
        }
        listener.onClickRestaurant(restaurantId, previousRestaurantId,crmId)

        this.dismiss()
    }

}
