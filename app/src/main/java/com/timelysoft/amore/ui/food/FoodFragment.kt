package com.timelysoft.amore.ui.food


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.category.CategoryAdapter
import com.timelysoft.amore.adapter.category.CategoryListener
import com.timelysoft.amore.adapter.shimmer.Layout
import com.timelysoft.amore.adapter.shimmer.ShimmeringAdapter
import com.timelysoft.amore.databinding.FragmentFoodBinding
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.Category
import com.timelysoft.amore.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class FoodFragment : BaseFragment(), CategoryListener {
    private val viewModel by activityViewModels<FoodViewModel>()
    private val categoryAdapter = CategoryAdapter(this)

    private val binding by viewBinding(FragmentFoodBinding::bind)

    private var categoryList: ArrayList<Category>? = null
    private var categoryId: String? = null
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            categoryName = requireArguments().getString("categoryName")
            categoryList = requireArguments().getParcelableArrayList("categories")
            categoryId = requireArguments().getString("CategoryId")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_food)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            init()
        }
        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()) {

                binding.foodCategoryRv.visibility = View.VISIBLE
                binding.group.visibility = View.VISIBLE
                binding.noInternetLayout.root.visibility = View.GONE
                init()
            }
        }

    }

    private fun init() {
        getSchedules()
        loadRestaurant()
        initData()

    }

    private fun getSchedules() {
        viewModel.getSchedules().observe(viewLifecycleOwner) { response ->

            response.doIfSuccess { scheduleResponse ->


                val dateLocale = Calendar.getInstance()

                val date = SimpleDateFormat("EEEE", Locale.ENGLISH).format(dateLocale.time)

                val weeks = hashMapOf(
                    "Monday" to 1, "Tuesday" to 2, "Wednesday" to 3,
                    "Thursday" to 4, "Friday" to 5, "Saturday" to 6,
                    "Sunday" to 0
                )
                val schedule = scheduleResponse.data.schedules.find {
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
            response.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
            }

            response.doIfNetwork { errorType ->
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

        }
    }

    private fun loadRestaurant() {
        viewModel.getRestaurantData().observe(viewLifecycleOwner, { restaurants ->

            restaurants.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                }
            }
            restaurants.doIfNetwork { errorType ->
                when (errorType) {
                    is ErrorTypes.TimeOutError -> {
                        toast(errorType.msg)
                    }
                    is ErrorTypes.ConnectionError -> {
                        binding.foodCategoryRv.visibility = View.GONE
                        binding.group.visibility = View.GONE
                        binding.noInternetLayout.root.visibility = View.VISIBLE
                        //toast(errorType.msg)
                    }
                    is ErrorTypes.EmptyResultError -> {
                        toast(errorType.msg)
                    }
                }

            }
            restaurants.doIfSuccess {

                val data = it.data
                AppPreferences.currencyName = data.currency.name
                AppPreferences.restaurant = data.id
                AppPreferences.bankPay = data.onlinePaymentSupported

                binding.restaurantDetailTitle.text = data.name

            }
        })


    }

    //keyAlias: amore password: amore123 password:0312490131Bo
    private fun initData() {
        viewModel.categoriesByRestaurantId()
            .observe(viewLifecycleOwner, { response ->

                response.doIfSuccess { categoriesResponse ->

                    categoryAdapter.set(categoriesResponse.data.categories as ArrayList<Category>)
                    binding.foodCategoryRv.apply {
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
                response.doIfLoading {
                    val shimmerAdapter = ShimmeringAdapter(Layout.Menu, 4)
                    binding.foodCategoryRv.apply {
                        adapter = shimmerAdapter
                    }
                }
                response.doIfError { errorBody ->
                    binding.foodCategoryRv.visibility = View.GONE
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }
                }
                response.doIfNetwork { errorType ->
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


    }


    override fun onCategoryClick(item: Category) {
        if (item.hasProducts) {
            val action = FoodFragmentDirections.toNavFoodItem(item.id, item.name)
            findNavController().navigate(action)
        }
    }

}
