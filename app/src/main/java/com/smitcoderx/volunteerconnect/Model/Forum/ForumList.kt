package com.smitcoderx.volunteerconnect.Model.Forum


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForumList(
    @SerializedName("data")
    var `data`: List<ForumData?> = listOf(),
    @SerializedName("success")
    var success: Boolean? = null
): Parcelable