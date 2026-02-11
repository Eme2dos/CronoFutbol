package com.eme2.cronofutbol.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat

fun startCronoService(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextCompat.startForegroundService(context, intent)
    } else {
        context.startService(intent)
    }
}