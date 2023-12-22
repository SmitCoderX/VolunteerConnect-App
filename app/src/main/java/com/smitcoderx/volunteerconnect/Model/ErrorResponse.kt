package com.smitcoderx.volunteerconnect.Model


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("success")
    val success: Boolean?
)