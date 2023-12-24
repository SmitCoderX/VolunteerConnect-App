package com.smitcoderx.volunteerconnect.Model


import com.google.gson.annotations.SerializedName
data class Login(
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("token")
    val token: String? = "",
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("message")
    val message: String? = ""

)