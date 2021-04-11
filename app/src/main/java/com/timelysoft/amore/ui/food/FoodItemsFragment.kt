package com.timelysoft.amore.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.timelysoft.amore.BasketCommands
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.FoodAdapter
import com.timelysoft.amore.adapter.food.FoodListener
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.databinding.FoodItemsFragmentBinding
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class FoodItemsFragment : BaseFragment(), FoodAddToBasket, FoodListener {

    private val viewModel: FoodItemsViewModel by viewModels()

    private var foodAdapter: FoodAdapter? = null
    private var categoryId: String? = null
    private val navArgs: FoodItemsFragmentArgs by navArgs()

    private val binding by viewBinding(FoodItemsFragmentBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.food_items_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbar.toolbarBack.setImageResource(R.drawable.ic_back)
        foodAdapter = FoodAdapter(this, this)


        binding.toolbar.toolbarText.text = navArgs.categoryName

        binding.toolbar.toolbarBack.visibility = View.VISIBLE

        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (!hasInitializedRootView){
            hasInitializedRootView = true
            loadData(navArgs.categoryId)

        }


        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()) {
                binding.foodRv.visibility = View.VISIBLE
                binding.noInternetLayout.root.visibility = View.GONE
                categoryId?.let { it1 -> loadData(it1) }
            }
        }

    }

    private fun loadData(categoryId: String) {
        loadingShow()
        viewModel.itemsByCategories(categoryId)
            .observe(viewLifecycleOwner, {
                it.doIfSuccess { itemsResponse ->
                    loadingHide()
                    foodAdapter?.set(itemsResponse.data.menuItems as ArrayList<MenuItem>)
                    binding.foodRv.adapter = foodAdapter
                }
                it.doIfError { errorBody ->
                    loadingHide()
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }

                }
                it.doIfNetwork {
                    loadingHide()
                    binding.foodRv.visibility = View.GONE
                    binding.noInternetLayout.root.visibility = View.VISIBLE
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
                            Mode.NotBasket
                        )
                    bottom.show(parentFragmentManager, bottom.tag)
                } else {
                    item.amount = 1
                    BasketCommands.addMenuItem(item, emptyList())
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
                            Mode.NotBasket
                        )
                    bottom.show(parentFragmentManager, bottom.tag)
                } else {
                    val bottom =
                        FoodAddUpdateBottomSheet(menuItem, Mode.NotBasket)
                    bottom.show(parentFragmentManager, bottom.tag)

                }
            } else {
                snackbar("Можете заказывать еду только в промежутке: ${AppPreferences.schedule}")

            }
        }
    }
}