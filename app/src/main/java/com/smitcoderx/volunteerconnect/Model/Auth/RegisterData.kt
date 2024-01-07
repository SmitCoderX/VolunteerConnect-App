package com.smitcoderx.volunteerconnect.Model.Auth

data class RegisterData(
    var username: String? = "",
    var name: String? = "",
    var email : String?  = "",
    var password: String? = "",
    var role: String? = "",
    var phoneNumber: String? = ""
)
