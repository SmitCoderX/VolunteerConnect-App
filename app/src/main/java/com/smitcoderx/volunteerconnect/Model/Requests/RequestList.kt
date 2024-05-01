package com.smitcoderx.volunteerconnect.Model.Requests


import com.google.gson.annotations.SerializedName

data class RequestList(
    @SerializedName("message")
    var message: List<RequestsData?>? = null,
    @SerializedName("success")
    var success: Boolean? = null
)