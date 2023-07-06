package com.konkuk.capture

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Capture state
 *
 * 캡쳐 상태 관리 클래스, rememberCaptureState를 통해서 생성 가능
 *
 * @constructor Create empty Capture state
 */
class CaptureState internal constructor() {

    val captureState = mutableStateOf<CaptureResult>(CaptureResult.Initialized)

    val bitmapState = mutableStateOf<Bitmap?>(null)

    val bitmap: Bitmap?
        get() = bitmapState.value

    val imageBitmap: ImageBitmap?
        get() = bitmap?.asImageBitmap()

    internal var block: (() -> Unit)? = null

    fun capture() {
        block?.invoke()
    }
}
