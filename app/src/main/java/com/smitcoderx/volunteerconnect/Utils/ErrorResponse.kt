package com.smitcoderx.volunteerconnect.Utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smitcoderx.volunteerconnect.Model.ErrorResponse
import retrofit2.Response

fun <T> errorResponse(response: Response<T>): ErrorResponse? {
    val gson = Gson()
    val type = object : TypeToken<ErrorResponse?>() {}.type
    return gson.fromJson(response.errorBody()!!.charStream(), type)
}