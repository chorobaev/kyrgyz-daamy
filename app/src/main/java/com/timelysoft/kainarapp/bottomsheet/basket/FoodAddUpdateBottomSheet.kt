package com.timelysoft.kainarapp.bottomsheet.basket

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.food.CustomAdapterForMod
import com.timelysoft.kainarapp.adapter.food.ItemModGroupListener
import com.timelysoft.kainarapp.extension.loadImageOrHide
import com.timelysoft.kainarapp.extension.toast
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import com.timelysoft.kainarapp.ui.food.FoodViewModel
import kotlinx.android.synthetic.main.fragment_food_order.*
import kotlin.collections.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel


class FoodAddUpdateBottomSheet(
    private var menuItem: MenuItem,
    private val position: Int? = null
) : BottomSheetDialogFragment(), ItemModGroupListener {

    private val viewModel: FoodViewModel by viewModel()
    private var count = 1
    private var listOfModGroup = listOf<BaseModifierGroup>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_food_order, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behaviour = BottomSheetBehavior.from(view)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        food_order_title.text = menuItem.name
        food_order_cost.text = menuItem.price.toString() + "сом"
        food_order_photo.loadImageOrHide(AppPreferences.baseUrl + "api/restaurants/${AppPreferences.restaurant}/menu/items/${menuItem.code}/image")
        food_order_description.visibility = View.VISIBLE
        food_order_description.text = menuItem.recipe

        fragment_order_recyclerView.adapter = CustomAdapterForMod(
            menuItem.modifierGroups as ArrayList<BaseModifierGroup>,
            this,
            false
        )


        food_order_minus.setOnClickListener {
            if (count > 1) {
                --count
                food_order_counter.text = count.toString()
                menuItem.amount = count
                //food_order_cost.text = countCost(count, menuItem.price)
            }
        }
        food_order_plus.setOnClickListener {
            if (menuItem.rests == -1 || menuItem.rests >= count) {
                ++count
                food_order_counter.text = count.toString()
                menuItem.amount = count
                //food_order_cost.text = countCost(count, menuItem.price)
            } else {
                toast(requireContext().getString(R.string.warning_beyound_range))
            }
        }


        food_order_accept.setOnClickListener {

            if (count == 1) {
                menuItem.amount = 1
            }
            position?.let {
                menuItem.positionInList = it
            }
            viewModel.insertMenuItem(menuItem, 0, listOfModGroup)

            dismiss()

        }
        food_order_refuse.setOnClickListener {
            dismiss()
        }
    }


    override fun addModGroup(group: List<BaseModifierGroup>) {
        listOfModGroup = group
    }


}