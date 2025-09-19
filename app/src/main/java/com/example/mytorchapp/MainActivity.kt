package com.example.mytorchapp

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.mytorchapp.ui.theme.MyTorchAppTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTorchAppTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {

                    DraggableImage()

                }
            }
        }
    }
}

@Composable
fun DraggableImage() {

    var isFlashOn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0] // Use the first camera (usually the back camera)

    fun toggleFlashlight(turnOn: Boolean) {
        try {
            cameraManager.setTorchMode(cameraId, turnOn) // Turn flashlight ON/OFF
            isFlashOn = turnOn
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var dragAmountX = 0f
    var dragAmountY = 0f

    var offsetXCopX = 0f
    var offsetYCopy = 0f

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val holder_y = 475f

    if(offsetY >= holder_y){
        isFlashOn = true
    }else{
        isFlashOn = false
    }
    toggleFlashlight(isFlashOn)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
//        Text(text = "$offsetX and $offsetY",
//            modifier = Modifier.padding(top = 100.dp))
        Image(painter = painterResource(id = R.drawable.holder), contentDescription = "holder image",
            modifier = Modifier.width(500.dp)
                .height(300.dp)
                .offset(x = 10.dp, y = 500.dp),
            contentScale = ContentScale.Fit)

        Image(
            painter = painterResource(id = if(offsetY>= holder_y)R.drawable.bulb_on else R.drawable.bulb_off),
            contentDescription = "Draggable Image",
            modifier = Modifier
                .background(Color.Transparent)
                .size(500.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetXCopX = offsetX
                        offsetYCopy = offsetY
                        if(offsetYCopy + dragAmount.y > 486f) {
                            dragAmountY = 0f
                        }else {
                            dragAmountY = dragAmount.y
                        }
                        if(offsetX + dragAmount.x > 150f || offsetX + dragAmount.x < -166f) {
                            dragAmountX = 0f
                        }else {
                            dragAmountX = dragAmount.x
                        }
                        offsetX += dragAmountX
                        offsetY += dragAmountY
                    }
                }
        )

//        Image(painter = painterResource(id = R.drawable.holder), contentDescription = "holder image",
//            modifier = Modifier.width(500.dp)
//                .height(300.dp)
//                .offset(x = 10.dp, y = 500.dp),
//            contentScale = ContentScale.Fit)
    }

}

@Preview
@Composable
fun Preview(){
    DraggableImage()
}


