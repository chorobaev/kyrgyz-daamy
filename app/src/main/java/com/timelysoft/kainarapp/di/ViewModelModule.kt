package com.timelysoft.kainarapp.di


import com.timelysoft.kainarapp.ui.discount.NewsViewModel
import com.timelysoft.kainarapp.ui.food.FoodViewModel
import com.timelysoft.kainarapp.ui.restaurant.RestaurantViewModel
import com.timelysoft.kainarapp.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { FoodViewModel(get()) }
    viewModel { RestaurantViewModel(get()) }
    viewModel { NewsViewModel(get())}
    viewModel { SplashViewModel(get()) }
}