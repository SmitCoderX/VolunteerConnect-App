package com.smitcoderx.volunteerconnect.Model.Forum


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forum(
    @SerializedName("data")
    var `data`: ForumData? = null,
    @SerializedName("success")
    var success: Boolean? = null
): Parcelable