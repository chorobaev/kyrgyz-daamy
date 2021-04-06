package com.timelysoft.amore.ui.food

import androidx.lifecycle.ViewModel
import com.timelysoft.amore.service.NetworkRepositoryMod
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FoodItemsViewModel @Inject constructor(private val networkRepositoryMod: NetworkRepositoryMod) : ViewModel(){




    fun itemsByCategories(id:String) = networkRepositoryMod.foodItemByCategoryId(id)
}