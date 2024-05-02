package com.smitcoderx.volunteerconnect.Model.Requests


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestsData(
    @SerializedName("answers")
    var answers: List<Map<String, String>?>? = null,
    @SerializedName("appliedBy")
    var appliedBy: String? = null,
    @SerializedName("appliedByProfile")
    var appliedByProfile: String? = null,
    @SerializedName("eventId")
    var eventId: String? = null,
    @SerializedName("eventName")
    var eventName: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("recipient")
    var recipient: String? = null,
    @SerializedName("requester")
    var requester: String? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("transactionId")
    var transactionId: String? = null,
    @SerializedName("__v")
    var v: Int? = null
) : Parcelable