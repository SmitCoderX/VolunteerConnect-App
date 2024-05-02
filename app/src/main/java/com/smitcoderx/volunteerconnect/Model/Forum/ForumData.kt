package com.smitcoderx.volunteerconnect.Model.Forum


import com.google.gson.annotations.SerializedName

data class ForumData(
    @SerializedName("event")
    var event: String? = null,
    @SerializedName("forumImg")
    var forumImg: String? = null,
    @SerializedName("forumName")
    var forumName: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("participants")
    var participants: List<String?>? = null,
    @SerializedName("posts")
    var posts: List<String?>? = null,
    @SerializedName("slug")
    var slug: String? = null,
    @SerializedName("userId")
    var userId: String? = null,
    @SerializedName("__v")
    var v: Int? = null
)