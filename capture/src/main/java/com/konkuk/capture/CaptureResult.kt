package com.konkuk.capture

import android.graphics.Bitmap

sealed class CaptureResult {
    object Initialized : CaptureResult()
    data class Success internal constructor(val bitmap: Bitmap) : CaptureResult()
    data class Error internal constructor(val exception: Exception) : CaptureResult()
}
