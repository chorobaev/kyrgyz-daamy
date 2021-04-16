package io.flaterlab.kyrgyzdaamy.ui.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.food.FoodAdapter
import io.flaterlab.kyrgyzdaamy.adapter.food.FoodListener
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.FoodAddUpdateBottomSheet
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.Mode
import io.flaterlab.kyrgyzdaamy.databinding.FoodItemsFragmentBinding
import io.flaterlab.kyrgyzdaamy.extension.*
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class FoodItemsFragment : BaseFragment(), FoodAddToBasket, FoodListener {

    private val viewModel: FoodItemsViewModel by viewModels()

    private val navArgs: FoodItemsFragmentArgs by navArgs()

    private val binding by viewBinding(FoodItemsFragmentBinding::bind)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*lifecycleScope.launchWhenCreated {
            viewModel.imagesLinkStateFlow.collect {
                urls = it
            }
        }

         */

        return getPersistentView(
            inflater,
            container,
            savedInstanceState,
            R.layout.food_items_fragment
        )
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMenuItems(navArgs.categoryId)
        //viewModel.getItemImages(navArgs.categoryId)

        binding.toolbar.toolbarBack.setImageResource(R.drawable.ic_back)


        binding.toolbar.toolbarText.text = navArgs.categoryName

        binding.toolbar.toolbarBack.visibility = View.VISIBLE

        binding.toolbar.toolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            loadData()

        }


        binding.noInternetLayout.update.setOnClickListener {
            if (isConnectedOrConnecting()) {
                binding.foodRv.visibility = View.VISIBLE
                binding.noInternetLayout.root.visibility = View.GONE
                loadData()
            }
        }


    }

    private fun loadData() {
        loadingShow()

        lifecycleScope.launchWhenStarted {
            viewModel.menuItemsStateFlow.collectLatest {
                loadingHide()
                val foodAdapter = FoodAdapter(
                    this@FoodItemsFragment,
                    this@FoodItemsFragment,
                    it as ArrayList<MenuItem>
                )
                binding.foodRv.adapter = foodAdapter

            }
        }

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