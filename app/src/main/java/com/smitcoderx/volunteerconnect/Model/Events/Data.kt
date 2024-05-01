package com.smitcoderx.volunteerconnect.Model.Events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("category")
    var category: List<String?>? = listOf(),
    @SerializedName("coordinates")
    var coordinates: List<Double?>? = listOf(),
    @Transient
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("desc")
    var desc: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("eventEndingDateAndTime")
    var eventEndingDateAndTime: String? = "",
    @SerializedName("eventPicture")
    var eventPicture: String? = "",
    @Transient
    @SerializedName("eventPoint")
    var eventPoint: Int? = null,
    @SerializedName("eventStartDataAndTime")
    var eventStartDataAndTime: String? = "",
    @Transient
    @SerializedName("forumId")
    var forumId: String? = "",
    @SerializedName("documentType")
    var documentType: String? = "",
    @SerializedName("forumName")
    var forumName: String? = "",
    @SerializedName("gallery")
    var gallery: List<String> = listOf(),
    @SerializedName("goodies")
    var goodies: String? = "",
    @Transient
    @SerializedName("_id")
    var id: String? = "",
    @SerializedName("isForumCreated")
    var isForumCreated: Boolean? = false,
    @SerializedName("isGoodiesProvided")
    var isGoodiesProvided: Boolean? = false,
    @SerializedName("isPaid")
    var isPaid: Boolean? = false,
    @SerializedName("isPaying")
    var isPaying: Boolean? = false,
    @SerializedName("isResource")
    var isResource: Boolean? = false,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("payment")
    var payment: Int? = 0,
    @SerializedName("phone")
    var phone: String? = "",
    @SerializedName("price")
    var price: Int? = 0,
    @SerializedName("question")
    var question: List<String?>? = listOf(),
    @SerializedName("skills")
    var skills: List<String?>? = listOf(),
    @SerializedName("slug")
    var slug: String? = "",
    @Transient
    @SerializedName("user")
    var user: String? = "",
    @Transient
    @SerializedName("__v")
    var v: Int? = 0,
    @SerializedName("visibility")
    var visibility: String? = "",
    @SerializedName("volunteerCount")
    var volunteerCount: Int? = 0,
    @SerializedName("volunteers")
    var volunteers: List<String?>? = listOf()
) : Parcelable