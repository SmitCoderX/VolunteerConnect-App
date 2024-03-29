package com.smitcoderx.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prev(
    @SerializedName("limit")
    var limit: Int? = null,
    @SerializedName("page")
    var page: Int? = null
): Parcelable