package com.timelysoft.amore.ui.discount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.NetworkRepositoryMod
import com.timelysoft.amore.service.model.BaseResponse
import com.timelysoft.amore.service.response.NewsResponse

class NewsViewModel(val network : NetworkRepositoryMod) : ViewModel() {


    fun news(restaurantId: String = ""): LiveData<ApiResult<BaseResponse<List<NewsResponse>>?>> {
        return if (restaurantId == "") {
            network.news("")
        } else {
            network.news(restaurantId)
        }

    }

    fun groupNews() : LiveData<ApiResult<BaseResponse<List<NewsResponse>>?>>{
        return network.groupNews()
    }


}