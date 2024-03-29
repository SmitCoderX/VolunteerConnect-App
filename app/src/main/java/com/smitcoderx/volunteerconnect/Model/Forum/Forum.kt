package com.smitcoderx.volunteerconnect.Model.Forum


import com.google.gson.annotations.SerializedName

data class Forum(
    @SerializedName("data")
    var `data`: ForumData? = null,
    @SerializedName("success")
    var success: Boolean? = null
)