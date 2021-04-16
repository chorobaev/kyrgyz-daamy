package io.flaterlab.kyrgyzdaamy.ui.search

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.response.MenuItem
import io.flaterlab.kyrgyzdaamy.ui.FirebaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :ViewModel() {

    private var menuItemsMutableFlow : MutableStateFlow<List<MenuItem>> = MutableStateFlow(
        arrayListOf())

    val menuItemsStateFlow : StateFlow<List<MenuItem>>
        get() = menuItemsMutableFlow
    init {
        viewModelScope.launch{
            firebaseRepository.getAllMenuItems()
                .catch {exp->
                    exp.message?.let { Log.d("Exception in menu_items", it) }
                }
                .collect{
                if (it != null) {
                    menuItemsMutableFlow.value = it
                }
            }
        }
    }
}