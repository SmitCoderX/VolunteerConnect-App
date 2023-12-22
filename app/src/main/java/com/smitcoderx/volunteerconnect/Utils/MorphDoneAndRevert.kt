package com.smitcoderx.volunteerconnect.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import com.github.leandroborgesferreira.loadingbutton.animatedDrawables.ProgressType
import com.github.leandroborgesferreira.loadingbutton.customViews.ProgressButton
import com.smitcoderx.volunteerconnect.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UseCompatLoadingForDrawables")
fun ProgressButton.morphDoneAndRevert(
    context: Context,
    fillColor: Int,
    bitmap: Bitmap,
    revertTime: Long = 1500,
    coroutineScope: CoroutineScope,
    callback: () -> Unit
) {

    coroutineScope.launch(Dispatchers.Main) {
        progressType = ProgressType.INDETERMINATE
        startAnimation()
        delay(revertTime)
        doneLoadingAnimation(fillColor, bitmap)
        revertAnimation()
        callback()
    }
    context.getDrawable(R.drawable.bg_rounded_tab_look)?.let { setBackground(it) }
}