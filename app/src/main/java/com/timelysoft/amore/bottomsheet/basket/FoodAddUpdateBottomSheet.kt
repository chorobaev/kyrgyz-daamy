package com.timelysoft.amore.bottomsheet.basket

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.CustomAdapterForMod
import com.timelysoft.amore.adapter.food.ItemModGroupListener
import com.timelysoft.amore.extension.loadImageCoil
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.BaseModifierGroup
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.food.FoodViewModel
import kotlinx.android.synthetic.main.fragment_food_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class FoodAddUpdateBottomSheet(
    private var menuItem: MenuItem,
    private val mode: Mode,
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
        val view =  inflater.inflate(R.layout.fragment_food_order, container, false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = parentLayout?.let { it1 -> BottomSheetBehavior.from(it1) }
            if (menuItem.modifierGroups.isNotEmpty()){
                val layoutParams = parentLayout?.layoutParams

                layoutParams?.height = getBottomSheetDialogDefaultHeight()
                parentLayout?.layoutParams = layoutParams
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED

            }else{
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }

/*
            parentLayout?.let { view ->
                val behaviour = BottomSheetBehavior.from(view)

                //behaviour.state = BottomSheetBehavior.STATE_EXPANDED

                behaviour.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING){
                            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        bottomSheet.sl
                    }

                })

                 */
        }
        return dialog
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 85 / 100
    }
    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mode.name == "Editable"){
            if (menuItem.amount == 0) {
                food_order_counter.text = "1"
            }
            else{
                food_order_counter.text = menuItem.amount.toString()
                count = menuItem.amount
            }
        }
        if (mode == Mode.Editable){
            food_order_accept.text = "Изменить"
        }
        food_order_title.text = menuItem.name
        food_order_cost.text = menuItem.price.toString() + " ${AppPreferences.currencyName}"
        food_order_photo.loadImageCoil("https://images.carbis.ru/amore/${menuItem.code}.png")
        food_order_description.visibility = View.VISIBLE
        food_order_description.text = menuItem.recipe

        if (menuItem.modifierGroups.isNotEmpty()) {
            fragment_order_recyclerView.adapter = CustomAdapterForMod(
                menuItem.modifierGroups as ArrayList<BaseModifierGroup>,
                mode,
                this
            )
        }


        food_order_minus.setOnClickListener {
            if (count > 1) {
                if (mode == Mode.Editable) {
                    --count
                    food_order_counter.text = count.toString()
                } else {
                    --count
                    food_order_counter.text = count.toString()
                    menuItem.amount = count
                }
            }

        }
        food_order_plus.setOnClickListener {
            if (menuItem.rests == -1 || menuItem.rests >= count) {
                if (mode == Mode.Editable) {
                    ++count
                    food_order_counter.text = count.toString()
                }
                else {
                    ++count
                    food_order_counter.text = count.toString()
                    menuItem.amount = count
                }
            } else {
                toast(requireContext().getString(R.string.warning_beyound_range))
            }

        }


        food_order_accept.setOnClickListener {
            var isValid = true
            if (listOfModGroup.isEmpty()){
                menuItem.modifierGroups.forEach {
                    if (it.modifiersList.isNullOrEmpty() && it.minimumSelected>0){
                        isValid = false
                        toast("Выберите не менее ${it.minimumSelected} модификаторов")
                    }
                }
            }else {
                listOfModGroup.forEach {
                    if (it.minimumSelected > getModifierCount(it)) {
                        isValid = false
                        toast("Выберите не менее ${it.minimumSelected} модификаторов ")
                    }
                }
            }
            menuItem.amount = count
            if (count == 1) {
                menuItem.amount = 1
            }
            position?.let {
                menuItem.positionInList = it
            }
            if (isValid) {
                viewModel.insertMenuItem(menuItem, 0, listOfModGroup, mode)
                dismiss()
            }


        }
        food_order_refuse.setOnClickListener {
            dismiss()
        }
    }


    override fun addModGroup(group: List<BaseModifierGroup>) {
        listOfModGroup = group
    }

    fun getModifierCount(baseModifierGroup: BaseModifierGroup) : Int{
        var count = 0
        baseModifierGroup.modifiers.forEach {
            count +=it.count
        }

        return count
    }


}
enum class Mode{
    Basket,
    Editable,
    NotBasket
}