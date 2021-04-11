package io.flaterlab.kyrgyzdaamy.extension

import com.google.gson.Gson
import io.flaterlab.kyrgyzdaamy.service.model.ErrorResponse
import okhttp3.ResponseBody

inline fun ResponseBody.getErrors(message: (String) -> Unit) {
    val responseError = Gson().fromJson(charStream(), ErrorResponse::class.java)
    var msg = ""
    responseError.errors.forEach {
        var string = ""
        it.value.forEach { msg ->
            string += "$msg "
        }
        msg += string

    }
    message(msg)
}

