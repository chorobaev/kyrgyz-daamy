package com.timelysoft.kainarapp.di


import com.timelysoft.kainarapp.ui.MainViewModel
import com.timelysoft.kainarapp.ui.auth.AuthViewModel
import com.timelysoft.kainarapp.ui.discount.NewsViewModel
import com.timelysoft.kainarapp.ui.food.FoodViewModel
import com.timelysoft.kainarapp.ui.history.HistoryViewModel
import com.timelysoft.kainarapp.ui.home.HomeViewModel
import com.timelysoft.kainarapp.ui.restaurant.RestaurantViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FoodViewModel(get()) }
    viewModel { RestaurantViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MainViewModel(get()) }
}