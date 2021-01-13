package com.timelysoft.kainarapp.adapter.restaurant

import com.timelysoft.kainarapp.service.model2.SocialNetwork
import com.timelysoft.kainarapp.service.response.SocialNetworkResponse

interface SocialListener {
    fun onSocialClicked (model : SocialNetwork)
}