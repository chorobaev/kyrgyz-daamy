package com.timelysoft.amore.ui.food


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.category.CategoryAdapter
import com.timelysoft.amore.adapter.category.CategoryListener
import com.timelysoft.amore.adapter.image.ImagePageAdapter
import com.timelysoft.amore.adapter.shimmer.Layout
import com.timelysoft.amore.adapter.shimmer.ShimmeringAdapter
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.Category
import com.timelysoft.amore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_food.view.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FoodFragment : BaseFragment(), CategoryListener{
    private val viewModel: FoodViewModel by sharedViewModel()
    private val categoryAdapter = CategoryAdapter(this)

    private var categoryList: ArrayList<Category>? = null
    private var categoryId: String? = null
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            categoryName = requireArguments().getString("categoryName")
            categoryList = requireArguments().getParcelableArrayList<Category>("categories")
            categoryId = requireArguments().getString("CategoryId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_food)

        view?.main_toolbar?.visibility = View.GONE

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            initToolbar()
            init()
            update.setOnClickListener {
                if (isConnectedOrConnecting()) {
                    food_category_rv.visibility = View.VISIBLE
                    group.visibility = View.VISIBLE
                    noInternetLayout.visibility = View.GONE
                    init()
                }
            }


        }
    }

    private fun init(){

        getSchedules()
        loadRestaurant()
        initData()
    }

    private fun getSchedules() {
        viewModel.getSchedules().observe(viewLifecycleOwner, Observer { response ->

            response.doIfSuccess { scheduleResponse ->


                val dateLocale = Calendar.getInstance()

                val date = SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateLocale.time)

                val weeks = hashMapOf(
                    "Monday" to 1, "Tuesday" to 2, "Wednesday" to 3,
                    "Thursday" to 4, "Friday" to 5, "Saturday" to 6,
                    "Sunday" to 0
                )
                val schedule = scheduleResponse.schedules.find {
                    Schedules.scheduleList.add(it)
                    it.dayOfWeek == weeks[date]
                }

                if (schedule != null) {
                    val dateFrom = schedule.dateFrom.toHour().toDate()!!.formatTo("HH:mm")
                    val dateTo = schedule.dateTo.toHour().toDate()!!.formatTo("HH:mm")
                    val final = "$dateFrom - $dateTo"
                    AppPreferences.dateFrom = dateFrom
                    AppPreferences.dateTo = dateTo
                    AppPreferences.schedule = final

                    if (AppPreferences.lastDay == null || AppPreferences.lastDay != date) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Режим работы ресторана")
                            .setMessage("${resources.getString(R.string.message_alert)} с $dateFrom до $dateTo")
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                // Respond to positive button press
                                dialog.cancel()
                            }
                            .show()
                    }
                    AppPreferences.lastDay = date
                }
            }
            response.doIfError {errorBody ->
                errorBody?.getErrors { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun loadRestaurant() {
        viewModel.getRestaurantData().observe(viewLifecycleOwner, Observer { restaurants ->

            restaurants.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
            }
            restaurants.doIfNetwork { msg ->
                food_category_rv.visibility = View.GONE
                group.visibility = View.GONE
                noInternetLayout.visibility = View.VISIBLE
                //Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }
            restaurants.doIfSuccess {

                val urls = arrayListOf<String>()

                AppPreferences.currencyName = it.currency.name
                AppPreferences.restaurant = it.id
                AppPreferences.bankPay = it.onlinePaymentSupported
                restaurant_image.loadImageCoil(AppPreferences.baseUrl + it.files.first().relativeUrl)
                restaurant_detail_title.text = it.name

            }
        })


    }
//keyAlias: amore password: amore123 password:0312490131Bo
    private fun initData() {
        viewModel.categoriesByRestaurantId(AppPreferences.idOfRestaurant())
            .observe(viewLifecycleOwner, Observer { response ->

                response.doIfSuccess { categoriesResponse ->
                    if (categoriesResponse != null) {

                        categoryAdapter.set(categoriesResponse.data.categories as ArrayList<Category>)
                        food_category_rv.apply {
                            adapter = categoryAdapter
                            addItemDecoration(
                                CustomPositionItemDecoration(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.divider
                                    )!!
                                )
                            )
                            setHasFixedSize(true)
                        }
                    }
                }
                response.doIfLoading {
                    val shimmerAdapter = ShimmeringAdapter(Layout.Menu, 4)
                    food_category_rv.apply {
                        adapter = shimmerAdapter
                    }
                }
                response.doIfError { errorBody ->
                    food_category_rv.visibility = View.GONE
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }
                }

            })


    }

    private fun initToolbar() {

        toolbar_back.setOnClickListener {
                findNavController().popBackStack()
            }
    }


    override fun onCategoryClick(item: Category) {
        if (item.hasProducts) {
            viewModel.setCategoryId(item.id, item.name)
            findNavController().navigate(R.id.nav_food_item)

        }
    }
/*
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }



    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackbar("You are offline")

        } else {
            snackbar("You are onlne")
        }
    }
*/

}
