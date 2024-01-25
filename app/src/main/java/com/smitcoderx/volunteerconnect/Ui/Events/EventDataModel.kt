package com.smitcoderx.volunteerconnect.Ui.Events

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventDataModel(
    var title: String? = "",
    var desc: String? = "",
    var address: String? = "",
    var mobileNumber: String? = "",
    var email: String? = "",
    var volunteerCount: Int? = null,
    var volunteerSkills: MutableList<String> = mutableListOf(),
) : Parcelable
