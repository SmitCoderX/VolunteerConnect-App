package com.smitcoderx.volunteerconnect.Model.Events


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Events")
@Parcelize
data class DataFetch(
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("category")
    var category: List<String?>? = listOf(),
    @SerializedName("coordinates")
    var coordinates: List<Double?>? = listOf(),
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
    @SerializedName("eventPoint")
    var eventPoint: Int? = null,
    @SerializedName("eventStartDataAndTime")
    var eventStartDataAndTime: String? = "",
    @SerializedName("forumId")
    var forumId: String? = "",
    @SerializedName("forumName")
    var forumName: String? = "",
    @SerializedName("gallery")
    var gallery: List<String> = listOf(),
    @SerializedName("goodies")
    var goodies: String? = "",
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
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
    @SerializedName("documentType")
    var documentType: String? = "",
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
    @SerializedName("user")
    var user: String? = "",
    @SerializedName("__v")
    var v: Int? = 0,
    @SerializedName("visibility")
    var visibility: String? = "",
    @SerializedName("volunteerCount")
    var volunteerCount: Int? = 0,
    @SerializedName("volunteers")
    var volunteers: List<String?>? = listOf(),
    var isSaved: Boolean = false
) : Parcelable {
    constructor(
        address: String?, id: String, category: List<String?>?,
        coordinates: List<Double?>?, createdAt: String?, desc: String?,
        email: String?, eventEndingDateAndTime: String?, eventPicture: String?,
        eventPoint: Int?, eventStartDataAndTime: String?, forumId: String?,
        forumName: String?, gallery: List<String>?, goodies: String?,
        isForumCreated: Boolean?, isGoodiesProvided: Boolean?, isPaid: Boolean?,
        isPaying: Boolean?, isResource: Boolean?, name: String?, documentType: String?,
        payment: Int?, phone: String?, price: Int?, question: List<String?>?,
        skills: List<String?>?, slug: String?, user: String?, v: Int?,
        visibility: String?, volunteerCount: Int?, volunteers: List<String?>?, isSaved: Boolean
    )
            : this(
        address = address,
        category = category,
        coordinates = coordinates,
        createdAt = createdAt,
        desc = desc,
        email = email,
        eventEndingDateAndTime = eventEndingDateAndTime,
        eventPicture = eventPicture,
        eventPoint = eventPoint,
        eventStartDataAndTime = eventStartDataAndTime,
        forumId = forumId,
        forumName = forumName,
        gallery = gallery ?: listOf(),
        goodies = goodies,
        id = id,
        isForumCreated = isForumCreated,
        isGoodiesProvided = isGoodiesProvided,
        isPaid = isPaid,
        isPaying = isPaying,
        isResource = isResource,
        name = name,
        documentType = documentType,
        payment = payment,
        phone = phone,
        price = price,
        question = question,
        skills = skills,
        slug = slug,
        user = user,
        v = v,
        visibility = visibility,
        volunteerCount = volunteerCount,
        volunteers = volunteers,
        isSaved = isSaved
    )

}