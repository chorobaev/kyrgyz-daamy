package com.timelysoft.amore.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.BasketCommands
import com.timelysoft.amore.adapter.food.FoodAdapter
import com.timelysoft.amore.adapter.food.FoodListener
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.extension.getErrors
import com.timelysoft.amore.extension.loadingHide
import com.timelysoft.amore.extension.loadingShow
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.doIfError
import com.timelysoft.amore.service.doIfNetwork
import com.timelysoft.amore.service.doIfSuccess
import com.timelysoft.amore.service.model2.response2.MenuItem
import kotlinx.android.synthetic.main.app_toolbar.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FoodItemsFragment : Fragment(), FoodAddToBasket, FoodListener {
    private val viewModel: FoodViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.food_items_fragment, container, false)
        recyclerView = view.findViewById(R.id.food_rv)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_back.setImageResource(R.drawable.ic_back)

        val foodAdapter = FoodAdapter(this, this)

        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
         //   food_basket_price.text = "$it c"
        })
        viewModel.categoryNameLiveData.observe(viewLifecycleOwner, Observer {
            toolbar_text.text = "$it: "
        })
        toolbar_back.visibility = View.VISIBLE

        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }

        loadingShow()
        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {categoryId->
            viewModel.itemsByCategories(AppPreferences.restaurant, categoryId)
                .observe(viewLifecycleOwner, Observer {
                    it.doIfSuccess { itemsResponse ->
                        loadingHide(2000L)
                        foodAdapter.set(itemsResponse.menuItems as ArrayList<MenuItem>)
                        recyclerView.adapter = foodAdapter
                    }
                    it.doIfError { errorBody ->
                        loadingHide()
                        errorBody?.getErrors { msg ->
                            toast(msg)
                        }

                    }
                    it.doIfNetwork {msg->
                        loadingHide()
                        toast(msg)
                    }
                })
        })


    }

    override fun addToBasket(item: MenuItem, position: Int) {
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
            viewModel.insertMenuItem(item, 0, emptyList(),Mode.NotBasket)
        }
    }

    override fun onFoodClick(menuItem: MenuItem, position: Int) {
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
                FoodAddUpdateBottomSheet(menuItem, Mode.NotBasket,position)
            bottom.show(parentFragmentManager, bottom.tag)

        }
    }
}