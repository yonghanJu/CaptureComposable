package com.konkuk.capture

import android.graphics.Bitmap

sealed class CaptureResult {
    object Initialized : CaptureResult()
    data class Success(val bitmap: Bitmap) : CaptureResult()
    data class Error(val exception: Exception) : CaptureResult()
}
