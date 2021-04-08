package io.flaterlab.kyrgyzdaamy.ui.discount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.ApiResult
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import io.flaterlab.kyrgyzdaamy.service.model.BaseResponse
import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val network: NetworkRepositoryMod) : ViewModel() {


    fun news(restaurantId: String = ""): LiveData<ApiResult<BaseResponse<List<NewsResponse>>>> {
        return if (restaurantId == "") {
            network.news("")
        } else {
            network.news(restaurantId)
        }

    }

    fun groupNews(): LiveData<ApiResult<BaseResponse<List<NewsResponse>>>> {
        return network.groupNews()
    }


}