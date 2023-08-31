# CaptureComposable

[Yong's Blog Dev Dialog]

[![](https://jitpack.io/v/yonghanJu/CaptureComposable.svg)](https://jitpack.io/#yonghanJu/CaptureComposable)

[Yong's Blog Dev Dialog]: https://yonghanju.github.io/android/2023/07/06/Compose%EB%A1%9C-%EC%A1%B8%ED%94%84%EB%A7%8C%EB%93%A4%EA%B8%B0(4).html

Library for __capturing Composable components__

- __app module__ is demo app

- __capture module__ is Android Library for capturing composable contains and contains `CaptureState`, `CaptureResult`, `@Composable Capture`


## How to
To get a Git project into your build:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```kotlin
// root level settings.gradle.kts
repositories {
    // ...
    maven("https://jitpack.io")
}
```

### Step 2. Add the dependency
```kotlin
// module level build.gradle.kts 
dependencies {
    val latestVersion = "1.0.2" 
    implementation("com.github.yonghanJu:CaptureComposable:$latestVersion")
}
```

<br>

## Example Code
```kotlin
val captureState = rememberCaptureState()

Capture(
    modifier = Modifier
    captureState = captureState,
) {
    // @Composable content
}

// Captured
Button(onClick = { captureState.capture() })

// ...

// you can use these
captureState.bitmap // captured bitmap
captureState.state // capturedState(Initialized, Success(bitmap), Error(e))
```

## Demo App
<img src="https://github.com/yonghanJu/CaptureComposable/assets/65655825/f80e253a-74de-4d11-af78-c6a9b4ac270c" width="250" height="500"/>

