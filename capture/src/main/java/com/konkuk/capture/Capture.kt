package com.konkuk.capture

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import java.lang.Exception

/**
 * Capture
 *
 * @param modifier
 * @param captureState 캡쳐 상태, CaptureResult를 관리
 * @param content 캡쳐 가능한 Box 컴포저블 영역
 * @see CaptureState
 * @see CaptureResult
 * @receiver
 */
@Composable
fun Capture(
    modifier: Modifier = Modifier,
    captureState: CaptureState,
    content: @Composable () -> Unit,
) {
    val view: View = LocalView.current

    var composableRect by remember {
        mutableStateOf<Rect?>(null)
    }

    DisposableEffect(Unit) {
        captureState.captureBlock = {
            composableRect?.let { rect ->
                captureView(view, rect) { result ->
                    captureState.state.value = result
                }
            }
        }

        onDispose {
            captureState.bitmap?.apply {
                if (!isRecycled) {
                    recycle()
                }
            }
            captureState.captureBlock = null
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                composableRect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.boundsInWindow()
                } else {
                    it.boundsInRoot()
                }
            },
    ) {
        content()
    }
}

fun captureView(view: View, bounds: Rect, onCaptured: (CaptureResult) -> Unit) {
    runCatching {
        val bitmap = Bitmap.createBitmap(
            bounds.width.toInt(),
            bounds.height.toInt(),
            Bitmap.Config.ARGB_8888,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PixelCopy.request(
                (view.context as Activity).window,
                android.graphics.Rect(
                    bounds.left.toInt(),
                    bounds.top.toInt(),
                    bounds.right.toInt(),
                    bounds.bottom.toInt(),
                ),
                bitmap,
                {
                    when (it) {
                        PixelCopy.SUCCESS -> {
                            onCaptured(CaptureResult.Success(bitmap))
                        }
                        else -> {
                            onCaptured(CaptureResult.Error(Exception("Capturing Failed")))
                        }
                    }
                },
                Handler(Looper.getMainLooper()),
            )
        } else {
            val canvas = Canvas(bitmap)
                .apply {
                    translate(-bounds.left, -bounds.top)
                }
            view.draw(canvas)
            canvas.setBitmap(null)
        }
    }.onFailure {
        onCaptured(CaptureResult.Error(Exception(it)))
    }
}
