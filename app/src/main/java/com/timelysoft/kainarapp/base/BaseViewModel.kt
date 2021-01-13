package com.timelysoft.kainarapp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.timelysoft.kainarapp.service.NetworkRepositoryCRM
import com.timelysoft.kainarapp.service.NetworkRepositoryMod
import com.timelysoft.kainarapp.service.RetrofitClient
import com.timelysoft.kainarapp.service.db.AppDatabase
import com.timelysoft.kainarapp.service.db.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    
    protected val network = NetworkRepositoryMod(RetrofitClient.apiServiceMod())
    protected val networkCRM = NetworkRepositoryCRM(RetrofitClient.apiServiceCRM())
    protected val db = RoomRepository(AppDatabase.instance(application).dbDao())
    private val job: Job = SupervisorJob()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCleared() {
        super.onCleared()
        job.cancel() // Parent Job cancels all child coroutines.
    }


}