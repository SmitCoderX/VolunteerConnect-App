package com.smitcoderx.volunteerconnect.Model.Posts


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsDataList(
    @SerializedName("data")
    var `data`: List<PostsData?> = listOf(),
    @SerializedName("success")
    var success: Boolean? = null
): Parcelable