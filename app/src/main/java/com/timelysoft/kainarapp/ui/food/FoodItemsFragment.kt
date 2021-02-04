package com.timelysoft.kainarapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.adapter.food.FoodAdapter
import com.timelysoft.kainarapp.adapter.food.FoodListener
import com.timelysoft.kainarapp.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.kainarapp.bottomsheet.basket.Mode
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.loadingShow
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfNetwork
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
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