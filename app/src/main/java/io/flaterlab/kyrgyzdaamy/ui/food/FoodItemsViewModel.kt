package io.flaterlab.kyrgyzdaamy.ui.food

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import javax.inject.Inject


@HiltViewModel
class FoodItemsViewModel @Inject constructor(
    private val networkRepositoryMod: NetworkRepositoryMod
) : ViewModel() {

    fun itemsByCategories(id: String) = networkRepositoryMod.foodItemByCategoryId(id)
}