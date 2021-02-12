package com.timelysoft.amore.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.timelysoft.amore.R
import com.timelysoft.amore.adapter.basket.BasketAdapter
import com.timelysoft.amore.adapter.basket.BasketListener
import com.timelysoft.amore.adapter.food.BasketCommands
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateBottomSheet
import com.timelysoft.amore.bottomsheet.basket.FoodAddUpdateListener
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.extension.toast
import com.timelysoft.amore.service.model2.response2.MenuItem
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_basket.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BasketFragment : Fragment(), BasketListener, FoodAddUpdateListener {

    val viewModel: FoodViewModel by sharedViewModel()
    var menuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()

        basket_pay.setOnClickListener {
            if (!BasketCommands.basketIsEmpty()) {
                findNavController().navigate(R.id.nav_order)
            } else {
                toast("Ваша корзина пуста")
            }
        }

        initToolbar()

        BasketCommands.sumOfBasket.observe(viewLifecycleOwner, Observer {
            basket_sum.text = "$it сом"
        })


    }


    override fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int) {
        TODO("Not yet implemented")
    }



    private fun initData() {
        BasketCommands.liveDataOfMenuItems.observe(viewLifecycleOwner, Observer {
            val basketAdapter = BasketAdapter(this, it as ArrayList<MenuItem>)
            basket_rv.adapter = basketAdapter
        })
    }

    override fun onClickItem(item: MenuItem, position: Int) {
        menuItem = item


        val bottom =
            FoodAddUpdateBottomSheet(
                item,
                Mode.Editable,
                position

            )
        bottom.show(parentFragmentManager, bottom.tag)

    }

    override fun onDeleteItem(position: Int) {
        viewModel.deleteMenuItem(position)
    }
    private fun initToolbar() {
        val navHostFragment: NavHostFragment = this.parentFragment as NavHostFragment

        val count = navHostFragment.childFragmentManager.backStackEntryCount
        if (count > 0) {
            toolbar_back.visibility = View.VISIBLE
        } else {
            toolbar_text.text = getString(R.string.menu_basket)
        }
        toolbar_back.setOnClickListener {
            if (count > 0) {
                findNavController().popBackStack()
            }

        }
    }

}
