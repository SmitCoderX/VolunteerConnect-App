package com.smitcoderx.volunteerconnect.Model.Auth


import com.google.gson.annotations.SerializedName
data class Login(
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("token")
    val token: String? = "",
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("role")
    val role: String? = "",

)