package com.smitcoderx.volunteerconnect.Model.Forum


import com.google.gson.annotations.SerializedName

data class ForumList(
    @SerializedName("data")
    var `data`: List<ForumData?> = listOf(),
    @SerializedName("success")
    var success: Boolean? = null
)