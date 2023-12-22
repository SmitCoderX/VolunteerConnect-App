package com.smitcoderx.volunteerconnect.Utils

import android.app.Activity
import androidx.core.content.res.ResourcesCompat
import com.smitcoderx.volunteerconnect.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object Constants {
    const val TAG = "VolunteerConnect"

    fun getSuccessDarkToast(context: Activity, title: String, desc: String) {
        MotionToast.createColorToast(
            context,
            title,
            desc,
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.poppins)
        )
    }

    fun getInfoDarkToast(context: Activity, title: String, desc: String) {
        MotionToast.createColorToast(
            context,
            title,
            desc,
            MotionToastStyle.INFO,
            MotionToast.GRAVITY_CENTER,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.poppins)
        )
    }

    fun getErrorDarkToast(context: Activity, title: String, desc: String) {
        MotionToast.createColorToast(
            context,
            title,
            desc,
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.poppins)
        )
    }
}