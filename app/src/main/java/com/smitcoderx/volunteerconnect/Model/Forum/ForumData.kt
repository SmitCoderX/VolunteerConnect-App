package com.smitcoderx.volunteerconnect.Model.Forum


import com.google.gson.annotations.SerializedName

data class ForumData(
    @SerializedName("desc")
    var desc: String? = null,
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
    @SerializedName("slug")
    var slug: String? = null,
    @SerializedName("__v")
    var v: Int? = null
)