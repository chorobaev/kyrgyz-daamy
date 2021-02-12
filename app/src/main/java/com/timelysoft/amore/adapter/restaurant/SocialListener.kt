package com.timelysoft.amore.adapter.restaurant

import com.timelysoft.amore.service.model2.SocialNetwork

interface SocialListener {
    fun onSocialClicked (model : SocialNetwork)
}