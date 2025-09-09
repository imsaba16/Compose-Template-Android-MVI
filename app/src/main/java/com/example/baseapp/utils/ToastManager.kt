package com.example.baseapp.utils

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.apply
import kotlin.let

// Toast types with different colors and icons
enum class ToastType {
    SUCCESS, ERROR, INFO
}

// Toast data class to manage state
data class ToastData(
    val message: String,
    val type: ToastType = ToastType.INFO,
    val duration: Long = 3000L
)

// Toast state management singleton object
object ToastStateManager {
    private val _toastData = mutableStateOf<ToastData?>(null)
    val toastData = _toastData

    fun showToast(message: String, type: ToastType = ToastType.ERROR, duration: Long = 3000L) {
        _toastData.value = ToastData(message, type, duration)
    }

    fun hideToast() {
        _toastData.value = null
    }
}

@Composable
private fun Toast(toastData: ToastData, onDismiss: () -> Unit) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    // Auto dismiss the toast after duration
    LaunchedEffect(toastData) {
        delay(toastData.duration)
        visibleState.targetState = false
        delay(300) // Give time for exit animation
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(animationSpec = tween(300)) +
                    expandVertically(animationSpec = tween(300), expandFrom = Alignment.Top),
            exit = fadeOut(animationSpec = tween(300)) +
                    shrinkVertically(animationSpec = tween(300), shrinkTowards = Alignment.Top)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(color = Color(0xFFE6E6E6), shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = getColorForType(toastData.type).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(getColorForType(toastData.type))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getIconForType(toastData.type),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Text(
                        text = toastData.message,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Default
                    )
                }
            }
        }
    }
}

@Composable
fun getColorForType(type: ToastType): Color {
    return when (type) {
        ToastType.SUCCESS -> Color.Green
        ToastType.ERROR -> Color.Red
        ToastType.INFO -> Color.Yellow
    }
}

@Composable
fun getIconForType(type: ToastType): ImageVector {
    return when (type) {
        ToastType.SUCCESS -> Icons.Default.Check
        ToastType.ERROR -> Icons.Default.Warning
        ToastType.INFO -> Icons.Default.Info
    }
}

@Composable
fun ToastHost() {
    val toastData = remember { ToastStateManager.toastData }

    toastData.value?.let { data ->
        Toast(toastData = data) {
            ToastStateManager.hideToast()
        }
    }
}