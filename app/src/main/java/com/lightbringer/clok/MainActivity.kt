package com.lightbringer.clok

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lightbringer.clok.ui.theme.ClokTheme
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentHour: Int = Calendar.getInstance().get(Calendar.HOUR)
        val currentMinutes: Int = Calendar.getInstance().get(Calendar.MINUTE)
        val currentSeconds: Int = Calendar.getInstance().get(Calendar.SECOND)
        setContent {
            ClokTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Clock(currentHour, currentMinutes, currentSeconds)
                }
            }
        }
    }
}

@Composable
fun Clock(currentHour: Int, currentMinutes: Int, currentSeconds: Int) {
    var ticks by remember { mutableStateOf(currentSeconds) }
    var minutes by remember { mutableStateOf(currentMinutes) }
    var hours by remember { mutableStateOf(currentHour) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            ticks++
            ticks %= 60
            if (ticks == 0) {
                minutes++
                minutes %= 60
            }
            if (minutes == 0 && ticks == 0) {
                hours++
                hours %= 12
            }
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF11192E))
    ) {
        val boxWithConstrainsScope = this
        Canvas(modifier = Modifier) {
            val center = Offset(
                boxWithConstrainsScope.maxWidth.toPx() / 2,
                boxWithConstrainsScope.maxHeight.toPx() / 2
            )
            val radius = boxWithConstrainsScope.maxWidth.toPx() / 2 * 2 / 3
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    center.x,
                    center.y,
                    radius,
                    Paint().apply {
                        strokeWidth = 3.dp.toPx()
                        color = android.graphics.Color.parseColor("#040E25")
                        style = Paint.Style.FILL
                        setShadowLayer(150f, 0f, 0f, android.graphics.Color.argb(90, 255, 255, 255))
                    }
                )
            }
            var strokeWidth: Float
            var lineHeight: Float
            for (i in 0..360 step 6) {
                if (i % 30 == 0) {
                    lineHeight = 60f
                    strokeWidth = 1.dp.toPx()
                } else {
                    lineHeight = 40f
                    strokeWidth = Stroke.HairlineWidth
                }

                val angle = i.toFloat() * (PI / 180)
                val startX = center.x + radius * cos(angle).toFloat()
                val startY = center.y + radius * sin(angle).toFloat()
                val endX = center.x + (radius - lineHeight) * cos(angle).toFloat()
                val endY = center.y + (radius - lineHeight) * sin(angle).toFloat()
                drawLine(
                    color = Color.Gray,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth
                )
            }

            //draw Hour
            val hour = (((hours * 30) - 90F) * (PI / 180))
            val startX = center.x
            val startY = center.y
            val endX = center.x + 100 * cos(hour).toFloat()
            val endY = center.y + 100 * sin(hour).toFloat()
            drawLine(
                color = Color(0xFFF454FF),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 3.dp.toPx()
            )
            //draw Minutes
            val minutesAngle = (((minutes * 6) - 90F) * (PI / 180))
            val minutesStartX = center.x
            val minutesStartY = center.y
            val minutesEndX = center.x + 180 * cos(minutesAngle).toFloat()
            val minutesSEndY = center.y + 180 * sin(minutesAngle).toFloat()
            drawLine(
                color = Color(0xFFF454FF),
                start = Offset(minutesStartX, minutesStartY),
                end = Offset(minutesEndX, minutesSEndY),
                strokeWidth = 3.dp.toPx()
            )
            //draw Second
            val secondsAngle = (((ticks * 6) - 90F) * (PI / 180))
            val secondsStartX = center.x
            val secondsStartY = center.y
            val secondsEndX = center.x + 200 * cos(secondsAngle).toFloat()
            val secondsEndY = center.y + 200 * sin(secondsAngle).toFloat()
            drawLine(
                color = Color(0xFF05d9e8),
                start = Offset(secondsStartX, secondsStartY),
                end = Offset(secondsEndX, secondsEndY),
                strokeWidth = 3.dp.toPx()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    ClokTheme {
        Clock(0, 43, 25)
    }
}