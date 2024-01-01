package com.smitcoderx.volunteerconnect.Model


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("data")
    val `data`: List<CategoryData?>?,
    @SerializedName("success")
    val success: Boolean?
)