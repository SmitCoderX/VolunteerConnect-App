package com.smitcoderx.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDataArrayModel(
    @SerializedName("count")
    var count: Int? = 0,
    @SerializedName("data")
    var dataList: List<DataFetch>? = listOf(),
    @SerializedName("pagination")
    var pagination: Pagination? = Pagination(),
    @SerializedName("success")
    var success: Boolean? = false
): Parcelable