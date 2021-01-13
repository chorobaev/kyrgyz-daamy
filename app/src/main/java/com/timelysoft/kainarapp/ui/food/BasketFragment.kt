package com.timelysoft.kainarapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.basket.BasketAdapter
import com.timelysoft.kainarapp.adapter.basket.BasketListener
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.bottomsheet.basket.FoodAddUpdateListener
import com.timelysoft.kainarapp.extension.*
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_basket.*

class BasketFragment : Fragment(),BasketListener,FoodAddUpdateListener {

    val viewModel : FoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initToolbar()
        initData()

        basket_pay.setOnClickListener {
            if (!BasketCommands.basketIsEmpty()) {
                if (AppPreferences.isLogined) {
                    findNavController().navigate(R.id.nav_order)
                } else {
                    val bundle = Bundle()
                    bundle.putBoolean("back", true)
                    findNavController().navigate(R.id.nav_auth, bundle)
                }
            } else {
                toast("Ваша корзина пуста")
            }
        }
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
            basket_sum.text = "$it сом"
        })

    }


    override fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int) {
        TODO("Not yet implemented")
    }


    private fun initData() {
        loadingShow()
        BasketCommands.liveDataOfMenuItems.observe(viewLifecycleOwner, Observer {
            val basketAdapter = BasketAdapter(this, it as ArrayList<MenuItem>)
            basket_rv.adapter = basketAdapter
            loadingHide()
        })
    }

    override fun onClickItem(item: MenuItem) {

    }

    override fun onDeleteItem(position: Int) {
        viewModel.deleteMenuItem(position)
    }



/*
        viewModel.amount().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                basket_sum.text = "${it / 100} coм"
                AppPreferences.amount = (it / 100).toInt()
                valid = true
            } else {
                basket_sum.text = "0 сом"
                AppPreferences.amount = 0
                valid = false
            }

        })

    }

 */
/*
    private fun initToolbar() {
        toolbar_back.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_text.text = getString(R.string.menu_basket)
    }

    override fun onClickItem(item: BasketEntity) {
        val bottom =
            FoodAddUpdateBottomSheet(
                this,
                item
            )
        bottom.show(parentFragmentManager, bottom.tag)
    }

    override fun onDeleteItem(item: BasketEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.confirm_delete)
        builder.setPositiveButton(R.string.delete_ok) { _, _ ->
            item.quantity = 0;
            viewModel.updateBasket(item)
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->

        }
        val dialog = builder.create();
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }

        dialog.show()

    }
    override fun addOrUpdateFoodBasket(food: BasketEntity, count: Int) {
        if (food.rests == -1 || food.rests >= count) {
            food.quantity = count
            viewModel.updateBasket(food)
        } else {
            toast("Это блюдо можно заказать в ограниченном количестве")
        }
    }

 */

}
