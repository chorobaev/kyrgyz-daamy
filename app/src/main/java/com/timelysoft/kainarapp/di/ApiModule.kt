package com.timelysoft.kainarapp.di


import com.timelysoft.kainarapp.service.NetworkRepositoryCRM
import com.timelysoft.kainarapp.service.NetworkRepositoryMod
import com.timelysoft.kainarapp.service.RetrofitClient
import org.koin.dsl.module

val module = module {
    single { RetrofitClient.apiServiceMod() }
    single {NetworkRepositoryMod(get()) }
    single { RetrofitClient.apiServiceCRM() }
    single { NetworkRepositoryCRM(get()) }

}
