package com.smitcoderx.volunteerconnect.Model.Posts


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsData(
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("createdByName")
    var createdByName: String? = null,
    @SerializedName("createdByProfile")
    var createdByProfile: String? = null,
    @SerializedName("forumId")
    var forumId: String? = null,
    @SerializedName("forumName")
    var forumName: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("postDetails")
    var postDetails: String? = null,
    @SerializedName("postImg")
    var postImg: String? = null,
    @SerializedName("postName")
    var postName: String? = null,
    @SerializedName("__v")
    var v: Int? = null
): Parcelable