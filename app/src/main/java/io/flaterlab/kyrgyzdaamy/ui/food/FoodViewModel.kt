package io.flaterlab.kyrgyzdaamy.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.response.*
import io.flaterlab.kyrgyzdaamy.ui.FirebaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class FoodViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private var mutableStateFlow:MutableStateFlow<List<Category>> = MutableStateFlow(arrayListOf())

    val stateFlow: StateFlow<List<Category>>
        get() = mutableStateFlow

    init {
        viewModelScope.launch {
            firebaseRepository.getMenu().collect {
                if (it != null) {
                    mutableStateFlow.value = it
                }
            }

        }
    }
}