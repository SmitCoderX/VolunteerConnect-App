package com.smitcoderx.volunteerconnect.Model.Posts


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsDataResponse(
    @SerializedName("data")
    var `data`: PostsData? = null,
    @SerializedName("success")
    var success: Boolean? = null
) : Parcelable