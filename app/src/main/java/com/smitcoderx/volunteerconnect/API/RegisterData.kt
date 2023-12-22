package com.smitcoderx.volunteerconnect.API

data class RegisterData(
    var username: String? = "",
    var name: String? = "",
    var email : String?  = "",
    var password: String? = "",
    var role: String? = "",
    var phoneNumber: String? = ""
)
