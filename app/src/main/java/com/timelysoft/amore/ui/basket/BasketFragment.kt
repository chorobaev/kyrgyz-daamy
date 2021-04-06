package com.timelysoft.amore.ui.basket

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.BasketCommands
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.basket.BasketAdapter
import com.timelysoft.amore.adapter.basket.BasketListener
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateListener
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.databinding.FragmentBasketBinding
import com.timelysoft.amore.extension.*
import com.timelysoft.amore.service.*
import com.timelysoft.amore.service.response.MenuItem
import com.timelysoft.amore.ui.viewBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BasketFragment : Fragment(R.layout.fragment_basket), BasketListener {

    private val binding by viewBinding(FragmentBasketBinding::bind)
    var menuItem: MenuItem? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()

        binding.basketPay.setOnClickListener {
            if (!BasketCommands.basketIsEmpty()) {
                val date = Calendar.getInstance().time
                val time = date.formatTo("HH:mm")
                if (AppPreferences.dateFrom != null && AppPreferences.dateTo != null) {
                    if (time.checkInBetween(AppPreferences.dateFrom!!, AppPreferences.dateTo!!)) {

                        findNavController().navigate(R.id.nav_order)
                    } else {
                        snackbar("Вы можете сделать заказ в промежутке: ${AppPreferences.schedule}")
                    }


                } else {
                    toast("  корзина пуста")
                }
            }
        }

        initToolbar()

        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, {
            binding.basketSum.text = "$it ${AppPreferences.currencyName}"
        })


    }


    private fun initData() {
        BasketCommands.liveDataHashMap.observe(viewLifecycleOwner, {
            val items= it.values.map {item->
                item
            }
            val basketAdapter = BasketAdapter(this, items  as ArrayList<MenuItem>)
            binding.basketRv.adapter = basketAdapter
        })
    }

    override fun onClickItem(item: MenuItem) {

        val bottom =
            FoodAddUpdateBottomSheet(
                item,
                Mode.Editable
            )
        bottom.show(parentFragmentManager, bottom.tag)

    }

    override fun onDeleteItem(item: MenuItem) {
        BasketCommands.deleteFromBasket(item.toString())
    }

    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            binding.basketToolbar.toolbarBack.visibility = View.VISIBLE
        } else {
            binding.basketToolbar.toolbarText.text = getString(R.string.menu_basket)
        }
        binding.basketToolbar.toolbarBack.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }

}
