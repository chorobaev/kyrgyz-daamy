package com.timelysoft.kainarapp.ui.food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.kainarapp.adapter.category.CategoryAdapter
import com.timelysoft.kainarapp.adapter.category.CategoryListener
import com.timelysoft.kainarapp.adapter.food.AddToBasketListener
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.adapter.food.FoodAdapter
import com.timelysoft.kainarapp.adapter.food.FoodListener
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseListener
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseBottomSheet
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.*
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.model2.response2.BaseModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.Category
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_basket.*
import kotlinx.android.synthetic.main.fragment_basket.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_food.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodFragment : Fragment(), CategoryListener, FoodListener,FoodAddToBasket,
    RestaurantChooseListener {

    private val viewModel: FoodViewModel by viewModel()
    private val categoryAdapter = CategoryAdapter(this)
    private val foodAdapter = FoodAdapter(this, this)
    private var categoryList: ArrayList<Category>? = null
    private var hasItem: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            categoryList = requireArguments().getParcelableArrayList<Category>("categories")
            hasItem = requireArguments().getBoolean("hasItems")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_food, container, false)
        if (hasItem == false) {
            view.linearLayoutFood.visibility = View.GONE
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initPhoto()
        initAdapters()
        initButtons()

        if (categoryList != null) {
            categoryAdapter.set(categoryList!!)
        } else {
            initData()
        }


        //checkMenu()
        food_basket_price.text = "${AppPreferences.amount / 100} с"

        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
            food_basket_price.text = "${it}c"
        })


    }

    private fun initPhoto() {
        food_selected_restaurant.loadImageWithoutCorner(AppPreferences.restaurantPhoto)
    }


    private fun initAdapters() {
        food_category_rv.adapter = categoryAdapter
        food_rv.adapter = foodAdapter
    }

    private fun initButtons() {


        food_basket.setOnClickListener {
            if (BasketCommands.listOfMenuItems.size > 0) {
                findNavController().navigate(R.id.nav_basket)
            }
        }

        food_change_restaurant.setOnClickListener {
            showBottomSheet()
        }


    }

    private fun showBottomSheet() {
        loadingShow()
        viewModel.restaurants().observe(viewLifecycleOwner, Observer {response->
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


    private fun initData() {

        loadingShow()
        viewModel.categoriesByRestaurantId(AppPreferences.restaurant)
            .observe(viewLifecycleOwner, Observer {response->
                loadingHide()
                response.doIfSuccess {categoriesResponse->
                    if (categoriesResponse != null){
                        categoryAdapter.set(categoriesResponse.data.categories as ArrayList<Category>)
                    }
                }
                response.doIfError {errorBody->
                    errorBody?.getErrors {msg->
                        toast(msg)
                    }

                }
                response.doIfNetwork {
                    toast(it)
                }
            })

    }

    private fun getMenuItems(categoryId: String) {
        viewModel.itemsByCategories(AppPreferences.restaurant, categoryId)
            .observe(viewLifecycleOwner, Observer {
                it.doIfSuccess {itemsResponse->
                    foodAdapter.set(itemsResponse.menuItems as ArrayList<MenuItem>)
                }
                it.doIfError {errorBody->
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

        toolbar_text.text = getString(R.string.menu_food)
    }

    override fun onCategoryClick(item: Category) {
        restaurant_category_name.text = item.name
        val bundle = Bundle()

        if (item.categories != null) {
            bundle.putParcelableArrayList("categories", item.categories as ArrayList<Category>)
            bundle.putBoolean("hasItems", item.hasProducts)
            getMenuItems(item.id)
            findNavController().navigate(R.id.nav_food, bundle)
        }

        if (item.hasProducts) {
            linearLayoutFood.visibility = View.VISIBLE
            getMenuItems(item.id)
        }

    }


    override fun onFoodClick(menuItem: MenuItem, position: Int) {

        if (menuItem.modifierGroups.isNotEmpty()) {
            val bottom =
                FoodAddUpdateBottomSheet(
                    menuItem,
                    position
                )
            bottom.show(parentFragmentManager, bottom.tag)
        } else {
            val bottom =
                FoodAddUpdateBottomSheet(menuItem, position)
            bottom.show(parentFragmentManager,bottom.tag)

        }

    }


    override fun onClickRestaurant(restaurantId: String, previousRestaurantId: String, crmId: Int) {
        if (restaurantId != previousRestaurantId) {
            BasketCommands.deleteAll()
        }

        loadingShow()
        initPhoto()
        restaurant_category_name.text = "Все"

        viewModel.categoriesByRestaurantId(restaurantId)
            .observe(viewLifecycleOwner, Observer { menu ->

                menu.doIfSuccess { categoriesResponse ->
                    if (categoriesResponse != null) {
                        categoryAdapter.set(categoriesResponse.data.categories as ArrayList<Category>)
                        categoryAdapter.row = -1
                        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
                            food_basket_price.text = "${it}c"
                        })
                        food_category_rv.adapter = categoryAdapter
                        loadingHide(2000L)
                    }
                }

                menu.doIfError {
                    it?.getErrors {msg->
                        toast(msg)
                    }
                    loadingHide()
                }

                menu.doIfNetwork {
                    toast(it)
                }
            })
    }

    override fun addToBasket(item: MenuItem, position: Int) {
        if (item.modifierGroups.isNotEmpty()){
            val bottom =
                FoodAddUpdateBottomSheet(
                    item,
                    position
                )
            bottom.show(parentFragmentManager, bottom.tag)
        }
        else{
            item.amount = 1
            item.positionInList = position
            viewModel.insertMenuItemWithoutModifiers(item, 0, emptyList())
        }
    }


}
