package com.smitcoderx.volunteerconnect.Model.Category


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
): Parcelable