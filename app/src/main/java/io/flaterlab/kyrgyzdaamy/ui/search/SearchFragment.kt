package io.flaterlab.kyrgyzdaamy.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.adapter.food.FoodListener
import io.flaterlab.kyrgyzdaamy.adapter.search.SearchMenuItemAdapter
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.FoodAddUpdateBottomSheet
import io.flaterlab.kyrgyzdaamy.bottomsheet.basket.Mode
import io.flaterlab.kyrgyzdaamy.databinding.SearchFragmentBinding
import io.flaterlab.kyrgyzdaamy.extension.checkInBetween
import io.flaterlab.kyrgyzdaamy.extension.formatTo
import io.flaterlab.kyrgyzdaamy.extension.snackbar
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment), FoodListener {


    private val viewModel by viewModels<SearchViewModel>()
    private var adapter: SearchMenuItemAdapter?= null

    val binding by viewBinding(SearchFragmentBinding::bind)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.menuItemsStateFlow.collectLatest {
                adapter = SearchMenuItemAdapter(it as ArrayList<MenuItem>, this@SearchFragment)
                binding.rvSearch.adapter = adapter
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return true
            }

        })
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