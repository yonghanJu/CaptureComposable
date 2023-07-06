package com.konkuk.caputrecomposable

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.capture.Capture
import com.konkuk.capture.CaptureResult
import com.konkuk.capture.rememberCaptureState
import com.konkuk.caputrecomposable.ui.theme.CaputreComposableTheme
import com.konkuk.caputrecomposable.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaputreComposableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    SampleScreen()
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
internal fun SampleScreen() {
    Column {
        val progress = remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            flow {
                repeat(100) {
                    delay(30)
                    emit(it + 1)
                }
            }.collect {
                progress.value = it
            }
        }
        Text(text = "Hello! \nThis is Sample App Capturing Composable", style = Typography.h6)
        Spacer(modifier = Modifier.height(100.dp))

        val captureState = rememberCaptureState()
        Capture(
            modifier = Modifier
                .wrapContentWidth()
                .height(100.dp)
                .background(Color.Red),
            captureState = captureState,
        ) {
            Column {
                Button(onClick = {
                    captureState.capture()
                }) {
                    Text("capture RED")
                }
                LinearProgressIndicator(progress = progress.value / 100F)
                Text("${progress.value}%", style = Typography.h4)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Captured Image",
            style = Typography.h6,
        )
        CapturedImage(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            captureState.state.value,
        )
    }
}

@Composable
internal fun CapturedImage(
    modifier: Modifier = Modifier,
    captureResult: CaptureResult,
) {
    Box(modifier = modifier) {
        when (captureResult) {
            is CaptureResult.Success -> {
                Image(bitmap = captureResult.bitmap.asImageBitmap(), "image")
            }
            is CaptureResult.Error -> {
                Text(text = "CaptureResult Error")
            }
            is CaptureResult.Initialized -> {
                Text(text = "CaptureResult Initialized")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun DefaultPreview() {
    CaputreComposableTheme {
        SampleScreen()
    }
}
