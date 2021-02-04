package com.timelysoft.kainarapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.timelysoft.kainarapp.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.groupiex.plusAssign
import com.timelysoft.kainarapp.adapter.category.CategoryAdapter
import com.timelysoft.kainarapp.adapter.category.CategoryListener
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.adapter.image.ImagePageAdapter
import com.timelysoft.kainarapp.bottomsheet.chooseRestuarant.RestaurantChooseListener
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.extension.loadingHide
import com.timelysoft.kainarapp.extension.loadingShow
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfNetwork
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model2.response2.Category
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_food.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FoodFragment : Fragment(), CategoryListener,
    RestaurantChooseListener, OnExpandableAdapterClick, OnChildItemListener {
    private val viewModel: FoodViewModel by sharedViewModel()
    private val categoryAdapter = CategoryAdapter(this)
    private var categoryList: ArrayList<Category>? = null
    private lateinit var viewPager: ViewPager
    private var categoryId: String? = null
    private var categoryName: String? = null
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

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
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        viewPager = view.findViewById(R.id.restaurant_detail_image_viewPager)
        if (categoryList != null) {
            view.constraintLayout.visibility = View.GONE
        }
        else{
            view.main_toolbar.visibility = View.GONE
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (categoryList != null) {

            implementExpandableAdapter(generateExpandableHeader(categoryList!!))

        } else {
            loadRestaurant()
            //BasketCommands.deleteAll()
            initData()
        }


    }

    private fun loadRestaurant(  ) {
        viewModel.restaurants().observe(viewLifecycleOwner, Observer { restaurants ->

            restaurants.doIfError { errorBody ->
                 errorBody?.getErrors { msg ->
                    toast(msg)
                }
            }
            restaurants.doIfNetwork { msg ->
                toast(msg)

            }
            restaurants.doIfSuccess {
                val urls = arrayListOf<String>()
                if (it.firstOrNull() != null) {
                    AppPreferences.restaurant = it.first().id
                    AppPreferences.bankPay = it.first().onlinePaymentSupported

                    if (it.first().files.isEmpty()){
                        urls.add("")
                    }else {
                        it.first().files.forEach { file ->
                            urls.add(AppPreferences.baseUrl + file.relativeUrl)
                        }
                    }

                }
                val adapter = ImagePageAdapter(urls)
                viewPager.adapter = adapter
                if (urls.size == 1) {
                    tabLayout.visibility = View.GONE
                }
                tabLayout.setupWithViewPager(viewPager)
                restaurant_detail_title.text = it.first().name

            }
        })
    }


    private fun initData() {
        loadingShow()
        viewModel.categoriesByRestaurantId(AppPreferences.restaurant)
            .observe(viewLifecycleOwner, Observer { response ->
                response.doIfSuccess { categoriesResponse ->
                    loadingHide()
                    if (categoriesResponse != null) {

                        categoryAdapter.set(categoriesResponse.data.categories as ArrayList<Category>)
                        food_category_rv.apply {
                            adapter = categoryAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                response.doIfError { errorBody ->
                    loadingHide()
                    errorBody?.getErrors { msg ->
                        toast(msg)
                    }
                }
                response.doIfNetwork {
                    loadingHide()
                    toast(it)
                }
            })


    }

    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            toolbar_back.visibility = View.VISIBLE
            toolbar_text.text = categoryName
        } else {
            toolbar_text.text = getString(R.string.menu_food)
        }
        toolbar_back.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }

    private fun generateExpandableHeader(list: List<Category>): List<ExpandableHeaderItem> {
        return list.map {
            ExpandableHeaderItem(it, this)
        }
    }

    private fun implementExpandableAdapter(list: List<ExpandableHeaderItem>) {
        groupAdapter.clear()
        groupAdapter.apply {
            list.forEach { expandableHeader ->
                this.add(ExpandableGroup(expandableHeader).apply {
                    expandableHeader.category.categories?.forEach { subCategory ->
                        add(ChildItem(subCategory, this@FoodFragment))
                    }
                })
            }
        }
        food_category_rv.apply {
            adapter = groupAdapter
            addItemDecoration(
                CustomPositionItemDecoration(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.divider
                    )!!
                )
            )
        }
    }


    override fun onCategoryClick(item: Category) {

        if (item.categories != null) {

            val bundle = Bundle()
            bundle.putString("categoryName", item.name)
            bundle.putParcelableArrayList("categories", item.categories as ArrayList<Category>)
            findNavController().navigate(R.id.nav_food, bundle)

        }

    }


    override fun onClickRestaurant(restaurantId: String, previousRestaurantId: String, crmId: Int) {


    }

    override fun onItemClick(category: Category) {
        if (category.categories == null && category.hasProducts) {
            viewModel.setCategoryId(category.id, category.name)
            findNavController().navigate(R.id.nav_food_item)
        }

    }

    override fun onChildClick(category: Category) {
        when{
            category.categories != null ->{
                val bundle = Bundle()
                bundle.putParcelableArrayList("categories", this as ArrayList<Category>)
                findNavController().navigate(R.id.nav_food, bundle)
            }
            else->{
                if (category.hasProducts){
                    viewModel.setCategoryId(category.id, category.name)
                    findNavController().navigate(R.id.nav_food_item)
                }
            }
        }
    }

}
