package com.smitcoderx.volunteerconnect.Ui.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDataModel(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("success")
    var success: Boolean? = null
): Parcelable