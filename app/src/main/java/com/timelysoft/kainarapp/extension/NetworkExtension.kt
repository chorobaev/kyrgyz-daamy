package com.timelysoft.kainarapp.extension

import com.google.gson.Gson
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import okhttp3.ResponseBody

inline fun ResponseBody.getErrors(message : (String) -> Unit){
    val responseError = Gson().fromJson(charStream(), ErrorResponse::class.java)
    var msg = ""
    responseError.errors.forEach {
        var string = ""
        it.value.forEach {msg->
            string += "$msg "
        }
        msg += "${it.key}: $string "
    }
    message(msg)
}
inline fun ResponseBody.getCrmErrors(message : (String) -> Unit){
    val responseError = Gson().fromJson(charStream(), ErrorResponseCRM::class.java)
    var msg = ""
    responseError.hashMap.forEach {
        var string = ""
        it.value.forEach {msg->
            string += "$msg "
        }
        msg += "${it.key}: $string "
    }
    message(msg)
}

