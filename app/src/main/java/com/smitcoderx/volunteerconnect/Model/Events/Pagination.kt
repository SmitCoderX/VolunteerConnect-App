package com.smitcoderx.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pagination(
    @SerializedName("next")
    var next: Next? = null,
    @SerializedName("prev")
    var prev: Prev? = null,
): Parcelable