package com.timelysoft.amore.bottomsheet.basket

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.amore.BasketCommands
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.food.CustomAdapterForMod
import com.timelysoft.amore.adapter.food.ItemModGroupListener
import com.timelysoft.amore.databinding.FragmentFoodOrderBinding
import com.timelysoft.amore.extension.loadImageCoil
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.response.BaseModifierGroup
import com.timelysoft.amore.service.response.MenuItem


class FoodAddUpdateBottomSheet(
    private var menuItem: MenuItem,
    private val mode: Mode
) : BottomSheetDialogFragment(), ItemModGroupListener {

    private var count = 1
    private var listOfModGroup = listOf<BaseModifierGroup>()
    private lateinit var binding: FragmentFoodOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_food_order, container, false)

        binding = FragmentFoodOrderBinding.bind(view)
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

            }

        }
        return dialog
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 85 / 100
    }
    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mode.name == "Editable"){
            if (menuItem.amount == 0) {
                binding.foodOrderCounter.text = "1"
            }
            else{
                binding.foodOrderCounter.text = menuItem.amount.toString()
                count = menuItem.amount
            }
        }
        if (mode == Mode.Editable){
            binding.foodOrderAccept.text = "Изменить"
        }
        binding.foodOrderTitle.text = menuItem.name
        binding.foodOrderCost.text = menuItem.price.toString() + " ${AppPreferences.currencyName}"
        binding.foodOrderPhoto.loadImageCoil("https://images.carbis.ru/amore/${menuItem.code}.png")
        binding.foodOrderDescription.visibility = View.VISIBLE
        binding.foodOrderDescription.text = menuItem.recipe

        if (menuItem.modifierGroups.isNotEmpty()) {
            binding.fragmentOrderRecyclerView.adapter = CustomAdapterForMod(
                menuItem.modifierGroups as ArrayList<BaseModifierGroup>,
                mode,
                this
            )
        }


        binding.foodOrderMinus.setOnClickListener {
            if (count > 1) {
                if (mode == Mode.Editable) {
                    --count
                    binding.foodOrderCounter.text = count.toString()
                } else {
                    --count
                    binding.foodOrderCounter.text = count.toString()
                    menuItem.amount = count
                }
            }

        }
        binding.foodOrderPlus.setOnClickListener {
            if (menuItem.rests == -1 || menuItem.rests >= count) {
                if (mode == Mode.Editable) {
                    ++count
                    binding.foodOrderCounter.text = count.toString()
                }
                else {
                    ++count
                    binding.foodOrderCounter.text = count.toString()
                    menuItem.amount = count
                }
            } else {
                toast(requireContext().getString(R.string.warning_beyound_range))
            }

        }


        binding.foodOrderAccept.setOnClickListener {
            var isValid = true
            if (listOfModGroup.isEmpty()){
                menuItem.modifierGroups.forEach {
                    if (it.modifiersList.isNullOrEmpty() && it.minimumSelected>0){
                        isValid = false
                       // toast("Выберите не менее ${it.minimumSelected} модификаторов")
                    }
                }
            }else {
                listOfModGroup.forEach {
                    if (it.minimumSelected > getModifierCount(it)) {
                        isValid = false
                        //toast("Выберите не менее ${it.minimumSelected} модификаторов ")
                    }
                }
            }

            val prevItem = with(menuItem){
                MenuItem(code,description,isHit,modifierGroups,name,price,recipe,rests,imageLink,weight,amount,priceWithMod)
            }
            menuItem.amount = count
            if (listOfModGroup.isNotEmpty()) {
                menuItem.modifierGroups  = listOfModGroup
            }

            if (isValid) {
                if (mode == Mode.Editable){

                    BasketCommands.updateMenuItem(menuItem,prevItem.toString())
                }else{
                    BasketCommands.addMenuItem(menuItem,listOfModGroup)
                }

                dismiss()
            }
        }
        binding.foodOrderRefuse.setOnClickListener {
            dismiss()
        }
    }


    override fun addModGroup(group: List<BaseModifierGroup>) {
        Log.d("ModGroup",group.toString())
        listOfModGroup = group
    }

    private fun getModifierCount(baseModifierGroup: BaseModifierGroup) : Int{
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