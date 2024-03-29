package com.smitcoderx.volunteerconnect.Model.Category


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResponse(
    @SerializedName("data")
    val data: List<CategoryData?>?,
    @SerializedName("success")
    val success: Boolean?
): Parcelable