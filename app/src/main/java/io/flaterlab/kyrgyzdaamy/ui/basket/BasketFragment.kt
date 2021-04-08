package io.flaterlab.kyrgyzdaamy.ui.basket

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.basket.BasketAdapter
import io.flaterlab.kyrgyzdaamy.adapter.basket.BasketListener
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.FoodAddUpdateBottomSheet
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.Mode
import io.flaterlab.kyrgyzdaamy.databinding.FragmentBasketBinding
import io.flaterlab.kyrgyzdaamy.extension.checkInBetween
import io.flaterlab.kyrgyzdaamy.extension.formatTo
import io.flaterlab.kyrgyzdaamy.extension.snackbar
import io.flaterlab.kyrgyzdaamy.extension.toast
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import java.util.*

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
            val items = it.values.map { item ->
                item
            }
            val basketAdapter = BasketAdapter(this, items as ArrayList<MenuItem>)
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
