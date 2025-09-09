package com.example.baseapp.utils

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable

@Composable
fun windowInsetsTop() = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
@Composable
fun windowInsetsBottom() = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()