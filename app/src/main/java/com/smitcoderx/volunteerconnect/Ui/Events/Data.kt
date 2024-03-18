package com.smitcoderx.volunteerconnect.Ui.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Data(
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("category")
    var category: List<String?>? = null,
    @SerializedName("coordinates")
    var coordinates: List<Double?>? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("desc")
    var desc: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("eventPicture")
    var eventPicture: String? = null,
    @SerializedName("gallery")
    var gallery: List<String?>? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("isPaid")
    var isPaid: Boolean? = null,
    @SerializedName("isPaying")
    var isPaying: Boolean? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("payment")
    var payment: Int? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("price")
    var price: Int? = 0,
    @SerializedName("skills")
    var skills: List<String?>? = null,
    @SerializedName("slug")
    var slug: String? = null,
    @SerializedName("user")
    var user: String? = null,
    @SerializedName("__v")
    var v: Int? = null,
    @SerializedName("visibility")
    var visibility: String? = null,
    @SerializedName("volunteerCount")
    var volunteerCount: Int? = null,
    @SerializedName("isGoodiesProvided")
    var isGoodiesProvided: Boolean? = null,
    @SerializedName("goodies")
    var goodies: String? = null,
    @SerializedName("eventStartDataAndTime")
    var eventStartDataAndTime: Date? = null,
    @SerializedName("eventEndingDateAndTime")
    var eventEndingDateAndTime: Date? = null,
) : Parcelable