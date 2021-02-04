package com.timelysoft.kainarapp.ui.restaurant

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.image.ImagePageAdapter
import com.timelysoft.kainarapp.adapter.restaurant.ImageViewPagerAdapter
import com.timelysoft.kainarapp.adapter.restaurant.RestaurantInfoAdapter
import com.timelysoft.kainarapp.adapter.restaurant.SocialAdapter
import com.timelysoft.kainarapp.adapter.restaurant.SocialListener
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.*
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.SocialNetwork
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_restaurant_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException

class RestaurantDetailFragment : Fragment(), SocialListener {
    private val viewModel: RestaurantViewModel by viewModel()
    private var restaurantId = ""
    private var globalId = ""
    private var restaurantPhoto = ""
    private var restaurantLogo = ""
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
        viewPager = view.findViewById(R.id.restaurant_detail_image_viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initData()

        restaurant_detail_discount.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("restaurantId", restaurantId)
            findNavController().navigate(R.id.nav_discount, bundle)
        }

        restaurant_detail_menu.setOnClickListener {
            AppPreferences.restaurant = restaurantId
            AppPreferences.restaurantPhoto = restaurantPhoto
            AppPreferences.restaurantLogo = restaurantLogo
            findNavController().navigate(R.id.nav_food)
            //todo upload menu
        }
    }

    private fun initData() {
        try {
            restaurantId = requireArguments().getString("restaurantId", "")
            Log.d("restaurantId", restaurantId)
        } catch (e: Exception) {
            restaurantId = ""
        }

        restaurantPhoto = try {
            requireArguments().getString("restaurantPhoto")!!
        } catch (e: Exception) {
            ""
        }

        restaurantLogo = try {
            requireArguments().getString("restaurantLogo")!!
        } catch (e: Exception) {
            ""
        }

        loadingShow()
        viewModel.restaurant(restaurantId).observe(viewLifecycleOwner, Observer { result ->
            loadingHide()
            result.doIfSuccess { successResult ->
                val restaurant = successResult.data
                val urls = arrayListOf<String>()
                restaurant.files.forEach {
                    urls.add(AppPreferences.baseUrl + it.relativeUrl)
                }
                val adapter = ImagePageAdapter(urls)
                viewPager.adapter = adapter
                if (urls.isEmpty()) {
                    tabLayout.visibility = View.GONE
                }
                tabLayout.setupWithViewPager(viewPager)
                restaurant_detail_title.text = restaurant.name
                toolbar_text.text = restaurant.name


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    restaurant_detail_address_online.text = Html.fromHtml(
                        restaurant.restaurantDetail.onlineAddress,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                    restaurant_detail_address.text = Html.fromHtml(
                        restaurant.restaurantDetail.address,
                        Html.FROM_HTML_MODE_LEGACY
                    )

                    restaurant_detail_about.text = Html.fromHtml(
                        restaurant.restaurantDetail.about,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    restaurant_detail_address_online.text =
                        Html.fromHtml(restaurant.restaurantDetail.onlineAddress)
                    restaurant_detail_address.text =
                        Html.fromHtml(restaurant.restaurantDetail.address)
                    restaurant_detail_about.text =
                        Html.fromHtml(restaurant.restaurantDetail.about)
                }

                if (restaurant.socialNetworks.isNotEmpty()) {

                    restaurant_detail_social_card.visibility = View.VISIBLE
                    restaurant_detail_social_rv.adapter = SocialAdapter(
                        this,
                        restaurant.socialNetworks as ArrayList<SocialNetwork>
                    )
                }
                globalId = ""
            }
            result.doIfError {errorBody->
                loadingHide()
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
        toolbar_text.text = getString(R.string.menu_restaurant_detail)
    }

    override fun onSocialClicked(model: SocialNetwork) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(model.url)))
        } catch (e: Exception) {

        }
    }
}
