package com.smitcoderx.volunteerconnect.Model.Auth


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("success")
    val success: Boolean?
)