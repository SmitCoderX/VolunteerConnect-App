package com.smitcoderx.volunteerconnect.Model


import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("category")
    val category: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("__v")
    val v: Int?
)