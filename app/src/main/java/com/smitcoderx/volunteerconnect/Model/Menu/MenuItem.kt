package com.smitcoderx.volunteerconnect.Model.Menu

import android.graphics.drawable.Drawable

data class MenuItem(
    val label: String,
    val icon: Drawable?,
    val destinationId: Int
)
