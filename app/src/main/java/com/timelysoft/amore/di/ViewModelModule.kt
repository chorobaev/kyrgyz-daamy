package com.timelysoft.amore.di


import com.timelysoft.amore.ui.discount.NewsViewModel
import com.timelysoft.amore.ui.food.FoodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { FoodViewModel(get()) }
    viewModel { NewsViewModel(get())}
}