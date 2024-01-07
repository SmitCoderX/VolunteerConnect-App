package com.smitcoderx.volunteerconnect.Model.User



import com.google.gson.annotations.SerializedName

data class UserDataModel(
    @SerializedName("data")
    val data: UserData?,
    @SerializedName("success")
    val success: Boolean?
)