package com.smitcoderx.volunteerconnect.Model.Requests


import com.google.gson.annotations.SerializedName

data class Requests(
    @SerializedName("success")
    var success: Boolean? = null,
    @SerializedName("data")
    var `data`: RequestsData? = null,
    @SerializedName("message")
    var message: String? = null,
)