package com.smitcoderx.volunteerconnect.Model.Requests


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestsData(
    @SerializedName("answers")
    var answers: Map<String, String>? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("recipient")
    var recipient: String? = null,
    @SerializedName("requester")
    var requester: String? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("__v")
    var v: Int? = null
) : Parcelable