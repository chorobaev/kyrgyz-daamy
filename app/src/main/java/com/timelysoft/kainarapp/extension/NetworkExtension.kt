package com.timelysoft.kainarapp.extension

import com.google.gson.Gson
import com.timelysoft.kainarapp.errors.Errors
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.ErrorResponseCRM
import okhttp3.ResponseBody

inline fun ResponseBody.getErrors(message : (String) -> Unit){
    val responseError = Gson().fromJson(charStream(), ErrorResponse::class.java)
    var msg = ""
    responseError.errors.forEach {
        when(it.key){
            "Alert"->{
                var string = ""
                it.value.forEach { msg->
                    val comment = Errors.errors_hashMap[msg]
                    string += "$comment"
                }
                msg+=string
            }
            else->{
                var string = ""
                it.value.forEach {msg->
                    val comment = Errors.errors_hashMap[msg]
                    string += "$comment "
                }
                val comment = Errors.properties[it.key]
                msg += "${comment}: $string "
            }
        }

    }
    message(msg)
}

