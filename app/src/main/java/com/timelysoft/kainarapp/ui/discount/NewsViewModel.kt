package com.timelysoft.kainarapp.ui.discount

import android.app.Application
import androidx.lifecycle.LiveData
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.response2.NewsResponse

class NewsViewModel(application: Application) : BaseViewModel(application) {


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