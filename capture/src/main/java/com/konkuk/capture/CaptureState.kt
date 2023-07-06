package com.konkuk.capture

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * 캡쳐 상태 관리 클래스, rememberCaptureState를 통해서 생성 가능
 *
 * @constructor Create empty Capture state
 */
class CaptureState internal constructor() {

    val state = mutableStateOf<CaptureResult>(CaptureResult.Initialized)

    val bitmap: Bitmap?
        get() = if (state.value is CaptureResult.Success) (state.value as? CaptureResult.Success)?.bitmap else null

    val imageBitmap: ImageBitmap?
        get() = bitmap?.asImageBitmap()

    internal var captureBlock: (() -> Unit)? = null

    /**
     * Capture 컴포저블 영역을 캡쳐합니다.
     * @see Capture
     * @see CaptureState.state
     */
    fun capture() {
        captureBlock?.invoke()
    }
}

/**
 * Remember capture state
 *
 * CaptureState 인스턴스 반환
 * @see CaptureState
 */
@Composable
fun rememberCaptureState() = remember {
    CaptureState()
}
