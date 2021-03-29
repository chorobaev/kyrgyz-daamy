package com.timelysoft.amore.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.FoodAdapter
import com.timelysoft.amore.adapter.food.FoodListener
import com.timelysoft.amore.adapter.shimmer.Layout
import com.timelysoft.amore.adapter.shimmer.ShimmeringAdapter
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.food_items_fragment.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.ArrayList

class FoodItemsFragment : BaseFragment(), FoodAddToBasket, FoodListener {
    private val viewModel: FoodViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private var foodAdapter: FoodAdapter? = null
    private var categoryId:String ? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            getPersistentView(inflater, container, savedInstanceState, R.layout.food_items_fragment)
        recyclerView = view?.findViewById(R.id.food_rv)!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            toolbar_back.setImageResource(R.drawable.ic_back)
            foodAdapter = FoodAdapter(this, this)


            viewModel.categoryNameLiveData.observe(viewLifecycleOwner, Observer {
                toolbar_text.text = "$it: "
            })
            toolbar_back.visibility = View.VISIBLE

            toolbar_back.setOnClickListener {
                findNavController().popBackStack()
            }

            viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
                categoryId = it
                loadData(it)

            })

            update.setOnClickListener {
                if (isConnectedOrConnecting()){
                    food_rv.visibility = View.VISIBLE
                    noInternetLayout.visibility = View.GONE
                    categoryId?.let { it1 -> loadData(it1) }
                }
            }

        }


    }

    private fun loadData(categoryId: String){
        loadingShow()
        viewModel.itemsByCategories(categoryId)
            .observe(viewLifecycleOwner, Observer {
                it.doIfSuccess { itemsResponse ->
                    loadingHide()
                    foodAdapter?.set(itemsResponse.menuItems as ArrayList<MenuItem>)
                    recyclerView.adapter = foodAdapter
                    viewModel._categoryItemResponse.value = itemsResponse
                }
                it.doIfError { errorBody ->
                    loadingHide()
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }

                }
                it.doIfNetwork { msg ->
                    loadingHide()
                    food_rv.visibility = View.GONE
                    noInternetLayout.visibility = View.VISIBLE
                }

            })
    }

    override fun addToBasket(item: MenuItem, position: Int) {
        val date = Calendar.getInstance().time
        val time = date.formatTo("HH:mm")
        if (AppPreferences.dateFrom != null && AppPreferences.dateTo != null) {
            if (time.checkInBetween(AppPreferences.dateFrom!!, AppPreferences.dateTo!!)) {
                if (item.modifierGroups.isNotEmpty()) {
                    val bottom =
                        FoodAddUpdateBottomSheet(
                            item,
                            Mode.NotBasket,
                            position
                        )
                    bottom.show(parentFragmentManager, bottom.tag)
                } else {
                    item.amount = 1
                    item.positionInList = position
                    viewModel.insertMenuItem(item, 0, emptyList(), Mode.NotBasket)
                }
            } else {
                snackbar("Можете заказывать еду только в промежутке: ${AppPreferences.schedule}")
            }

        }


    }

    override fun onFoodClick(menuItem: MenuItem, position: Int) {

        val date = Calendar.getInstance().time
        val time = date.formatTo("HH:mm")
        if (AppPreferences.dateFrom != null && AppPreferences.dateTo != null) {
            if (time.checkInBetween(AppPreferences.dateFrom!!, AppPreferences.dateTo!!)) {
                if (menuItem.modifierGroups.isNotEmpty()) {
                    val bottom =
                        FoodAddUpdateBottomSheet(
                            menuItem,
                            Mode.NotBasket,
                            position
                        )
                    bottom.show(parentFragmentManager, bottom.tag)
                } else {
                    val bottom =
                        FoodAddUpdateBottomSheet(menuItem, Mode.NotBasket, position)
                    bottom.show(parentFragmentManager, bottom.tag)

                }
            } else {
                snackbar("Можете заказывать еду только в промежутке: ${AppPreferences.schedule}")

            }
        }
    }
}