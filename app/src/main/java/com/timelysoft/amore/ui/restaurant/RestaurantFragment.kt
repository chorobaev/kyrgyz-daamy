package com.timelysoft.amore.ui.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.restaurant.RestaurantAdapter
import com.timelysoft.amore.adapter.restaurant.RestaurantListener
import com.timelysoft.amore.extension.getErrors
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.doIfError
import com.timelysoft.amore.service.doIfSuccess
import com.timelysoft.amore.service.model2.RestaurantResponse
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_restaurant.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RestaurantFragment : Fragment(), RestaurantListener {

    private val viewModel: RestaurantViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initData()
    }

    private fun initData() {
        loadingShow()
        viewModel.restaurants().observe(viewLifecycleOwner, Observer { restaurant ->
            loadingHide()
            restaurant.doIfSuccess {
                val restaurants = it
                val adapter = RestaurantAdapter(
                    this,
                    restaurants as ArrayList<RestaurantResponse>
                )
                restaurant_rv.adapter = adapter
            }
            restaurant.doIfError {errorBody->
                errorBody?.getErrors {msg->
                    toast(msg)
                }
                
            }
        })

    }

    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_restaurant)
    }

    override fun onRestaurantClick(
        restaurantId: String,
        photo: String,
        logo: String,
        crmId: Int
    ) {
        val bundle = Bundle()
        bundle.putString("restaurantId", restaurantId)
        bundle.putString("restaurantPhoto", photo)
        bundle.putString("restaurantLogo", logo)
       // findNavController().navigate(R.id.nav_restaurant_detail, bundle)
    }

}
