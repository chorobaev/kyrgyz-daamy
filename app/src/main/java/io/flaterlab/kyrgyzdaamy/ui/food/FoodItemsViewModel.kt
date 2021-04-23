package io.flaterlab.kyrgyzdaamy.ui.food

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.FirebaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodItemsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var menuItemsFlow: MutableStateFlow<List<MenuItem>> = MutableStateFlow(arrayListOf())

    private var imagesLinksFlow : MutableStateFlow<List<String>> = MutableStateFlow(listOf())

    val menuItemsStateFlow: StateFlow<List<MenuItem>>
        get() = menuItemsFlow

    val imagesLinkStateFlow : StateFlow<List<String>>
    get() = imagesLinksFlow

    @ExperimentalCoroutinesApi
    fun getMenuItems(categoryId: String) {
        viewModelScope.launch {
            firebaseRepository.getMenuItem(categoryId)
                .catch { exception ->
                    Log.d("Exception_get_menu_item", exception.message!!)
                }
                .collect {
                    if (it != null) {
                        menuItemsFlow.value = it
                    }
                }
        }
    }

    fun getItemImages(categoryId: String) {
        viewModelScope.launch {
            firebaseRepository.getImagesForMenuItems(categoryId)
                .catch {exception->
                    Log.d("Exception",exception.message!!)
                }.collect {
                    imagesLinksFlow.value = it
                }
        }
    }
}